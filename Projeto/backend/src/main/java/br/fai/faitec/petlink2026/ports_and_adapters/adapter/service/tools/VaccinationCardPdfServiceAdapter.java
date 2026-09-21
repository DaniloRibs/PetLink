package br.fai.faitec.petlink2026.ports_and_adapters.adapter.service.tools;

import br.fai.faitec.petlink2026.domain.animal.AnimalModel;
import br.fai.faitec.petlink2026.domain.animal.FarmAnimalModel;
import br.fai.faitec.petlink2026.domain.animal.PetModel;
import br.fai.faitec.petlink2026.domain.user.UserModel;
import br.fai.faitec.petlink2026.domain.vaccine.VaccineModel;
import br.fai.faitec.petlink2026.ports_and_adapters.port.service.animal.FarmAnimalService;
import br.fai.faitec.petlink2026.ports_and_adapters.port.service.animal.PetService;
import br.fai.faitec.petlink2026.ports_and_adapters.port.service.tools.VaccinationCardPdfService;
import br.fai.faitec.petlink2026.ports_and_adapters.port.service.user.UserService;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfGState;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPCellEvent;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.Normalizer;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class VaccinationCardPdfServiceAdapter implements VaccinationCardPdfService {

    private static final String DOMESTIC_LOGO_PATH = "assets/PetLink_Logo_Marrom.png";
    private static final String FARM_LOGO_PATH = "assets/PetLink_Logo_Verde.png";

    private static final int MIN_TABLE_ROWS = 8;

    private static final float PAGE_MARGIN = 40f;
    private static final float HEADER_HEIGHT = 105f;

    private static final Color DOMESTIC_COLOR = new Color(0x6F, 0x4E, 0x37);
    private static final Color FARM_COLOR = new Color(0x3E, 0x7B, 0x3A);

    private static final Color ACCENT = new Color(0xE0, 0xB4, 0x5C);
    private static final Color TEXT = new Color(0x26, 0x32, 0x38);
    private static final Color MUTED = new Color(0x78, 0x86, 0x8C);
    private static final Color DANGER = new Color(0xC6, 0x28, 0x28);

    private static final String EMPTY = "—";
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Autowired
    private PetService petService;

    @Autowired
    private FarmAnimalService farmAnimalService;

    @Autowired
    private UserService userService;

    @Override
    public String generateVaccinationCardPdf(final int animalId, final String outputDirectory) throws IOException {

        // pet_model e farm_animal_model compartilham os ids de animal_model, entao o mesmo id
        // identifica um unico animal, seja ele pet ou animal de fazenda.
        final AnimalModel animal = findAnimal(animalId);

        if (animal == null) {
            return null;
        }

        final UserModel owner = userService.findById(animal.getOwnerId());
        final List<VaccineModel> vaccines = sortByApplicationDate(animal.getVaccines());
        final String animalName = displayName(animal);

        final Document doc = new Document(PageSize.A4, PAGE_MARGIN, PAGE_MARGIN, 135f, 60f);

        final String nomeArquivo = "carteira-vacinacao_" + fileSlug(animalName) + "_" + animal.getId() + ".pdf";
        final String caminhoCompleto = outputDirectory + nomeArquivo;

        final boolean farmAnimal = animal instanceof FarmAnimalModel;
        final Theme theme = new Theme(farmAnimal ? FARM_COLOR : DOMESTIC_COLOR);
        final Image logoImage = loadImage(farmAnimal ? FARM_LOGO_PATH : DOMESTIC_LOGO_PATH);

        try {
            final PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(caminhoCompleto));

            final String animalLabel = truncate(animalName + " (" + speciesLabel(animal) + ")", 40);

            writer.setPageEvent(new VaccinationCardPageEvent(
                    theme, logoImage, animalLabel, LocalDate.now().format(DATE_FORMAT)));

            doc.open();

            doc.add(buildSectionTitle("Identificação do animal", theme));
            doc.add(buildAnimalInfoCard(animal, owner, theme));

            doc.add(buildSectionTitle("Assinatura do responsável", theme));
            doc.add(buildSignatureBlock(animal, theme));

            doc.add(buildSectionTitle("Registro de vacinas", theme));
            doc.add(buildVaccinesTable(vaccines, theme));
        } catch (final DocumentException e) {
            throw new IOException("Erro ao gerar o PDF do cartão de vacinação", e);
        } finally {
            doc.close();
        }

        return caminhoCompleto;
    }

    private PdfPTable buildSectionTitle(final String text, final Theme theme) {
        final PdfPTable table = new PdfPTable(1);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(7f);

        final PdfPCell cell = new PdfPCell(new Phrase(text.toUpperCase(), font(11, Font.BOLD, theme.primaryDark)));
        cell.setBorder(Rectangle.BOTTOM);
        cell.setBorderColorBottom(ACCENT);
        cell.setBorderWidthBottom(1.5f);
        cell.setPaddingLeft(0f);
        cell.setPaddingBottom(5f);
        table.addCell(cell);

        return table;
    }

    private PdfPTable buildAnimalInfoCard(final AnimalModel animal, final UserModel owner, final Theme theme) {
        final PdfPTable info = new PdfPTable(new float[]{1.5f, 1.3f, 1f});
        info.setWidthPercentage(100);
        info.addCell(infoCell("Nome do animal", displayName(animal), 1));
        info.addCell(infoCell("Raça", animal.getBreed(), 1));
        info.addCell(infoCell("Espécie", speciesLabel(animal), 1));
        info.addCell(infoCell("Sexo", genderLabel(animal.getGender()), 1));
        info.addCell(infoCell("Data de nascimento", formatDate(animal.getBirthDate()), 1));
        info.addCell(infoCell("Idade", ageLabel(animal.getBirthDate()), 1));

        if (animal instanceof FarmAnimalModel) {
            final FarmAnimalModel farmAnimal = (FarmAnimalModel) animal;
            info.addCell(infoCell("Identificador", farmAnimal.getIdentify(), 1));
            info.addCell(infoCell("Peso", farmAnimal.getWeight() > 0 ? formatWeight(farmAnimal.getWeight()) : EMPTY, 2));
        }

        info.addCell(infoCell("Tutor", owner == null ? EMPTY : owner.getFullname(), 3));

        final PdfPCell wrapper = new PdfPCell(info);
        wrapper.setBorder(Rectangle.NO_BORDER);
        wrapper.setPadding(4f);
        wrapper.setCellEvent(new RoundedBoxEvent(theme.softBg, theme.line));

        final PdfPTable card = new PdfPTable(1);
        card.setWidthPercentage(100);
        card.addCell(wrapper);

        return card;
    }

    private PdfPCell infoCell(final String label, final Object value, final int colspan) {
        final Paragraph content = new Paragraph();
        content.setLeading(14f);
        content.add(new Chunk(label.toUpperCase(), font(7.5f, Font.BOLD, MUTED)));
        content.add(Chunk.NEWLINE);
        content.add(new Chunk(safe(value), font(12, Font.BOLD, TEXT)));

        final PdfPCell cell = new PdfPCell(content);
        cell.setColspan(colspan);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPadding(7f);
        cell.setPaddingLeft(10f);
        return cell;
    }

    private PdfPTable buildSignatureBlock(final AnimalModel animal, final Theme theme) {
        final Paragraph declaration = new Paragraph();
        declaration.setLeading(12f);
        declaration.add(new Chunk("Declaro que as vacinas registradas nesta carteira foram aplicadas no animal ",
                font(9, Font.NORMAL, TEXT)));
        declaration.add(new Chunk(displayName(animal), font(9, Font.BOLD, TEXT)));
        declaration.add(new Chunk(" (" + speciesLabel(animal) + "). As doses sem assinatura individual "
                + "são validadas por esta assinatura.", font(9, Font.NORMAL, TEXT)));

        final PdfPTable grid = new PdfPTable(new float[]{2.4f, 0.2f, 1.2f, 0.2f, 1.1f});
        grid.setWidthPercentage(100);

        final PdfPCell declarationCell = new PdfPCell(declaration);
        declarationCell.setColspan(5);
        declarationCell.setBorder(Rectangle.NO_BORDER);
        declarationCell.setPadding(0f);
        declarationCell.setPaddingBottom(4f);
        grid.addCell(declarationCell);

        for (int c = 0; c < 5; c++) {
            grid.addCell(signatureLineCell(c % 2 == 0));
        }

        final String[] labels = {"Assinatura e carimbo do médico-veterinário", "", "CRMV", "", "Data"};
        for (final String label : labels) {
            grid.addCell(signatureLabelCell(label));
        }

        final PdfPCell wrapper = new PdfPCell(grid);
        wrapper.setBorder(Rectangle.NO_BORDER);
        wrapper.setPadding(10f);
        wrapper.setCellEvent(new RoundedBoxEvent(null, theme.primary));

        final PdfPTable block = new PdfPTable(1);
        block.setWidthPercentage(100);
        block.addCell(wrapper);

        return block;
    }

    private PdfPCell signatureLineCell(final boolean drawLine) {
        final PdfPCell cell = new PdfPCell(new Phrase(""));
        cell.setMinimumHeight(34f);
        if (drawLine) {
            cell.setBorder(Rectangle.BOTTOM);
            cell.setBorderColorBottom(MUTED);
            cell.setBorderWidthBottom(0.8f);
        } else {
            cell.setBorder(Rectangle.NO_BORDER);
        }
        return cell;
    }

    private PdfPCell signatureLabelCell(final String text) {
        final PdfPCell cell = new PdfPCell(new Phrase(text, font(7.5f, Font.NORMAL, MUTED)));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPadding(0f);
        cell.setPaddingTop(3f);
        return cell;
    }

    private PdfPTable buildVaccinesTable(final List<VaccineModel> vaccines, final Theme theme) {
        final PdfPTable table = new PdfPTable(new float[]{0.6f, 2.8f, 1.4f, 1.4f, 1.4f, 2.2f});
        table.setWidthPercentage(100);
        table.setHeaderRows(1);

        table.addCell(headerCell("Nº", theme));
        table.addCell(headerCell("Vacina", theme));
        table.addCell(headerCell("Lote", theme));
        table.addCell(headerCell("Aplicação", theme));
        table.addCell(headerCell("Validade", theme));
        table.addCell(headerCell("Veterinário / Carimbo", theme));

        final LocalDate today = LocalDate.now();
        final int totalRows = Math.max(vaccines.size(), MIN_TABLE_ROWS);

        for (int i = 0; i < totalRows; i++) {
            final Color background = (i % 2 == 0) ? null : theme.zebra;
            final VaccineModel vaccine = i < vaccines.size() ? vaccines.get(i) : null;

            table.addCell(bodyCell(new Phrase(String.valueOf(i + 1), font(9, Font.NORMAL, MUTED)),
                    background, Element.ALIGN_CENTER, theme));

            if (vaccine == null) {
                for (int c = 0; c < 5; c++) {
                    table.addCell(bodyCell(new Phrase(""), background, Element.ALIGN_LEFT, theme));
                }
                continue;
            }

            table.addCell(bodyCell(vaccineNameAndDescription(vaccine), background, Element.ALIGN_LEFT, theme));

            table.addCell(bodyCell(new Phrase(safe(vaccine.getBatch()), font(9, Font.NORMAL, TEXT)),
                    background, Element.ALIGN_CENTER, theme));

            table.addCell(bodyCell(new Phrase(formatDate(vaccine.getApplicationDate()), font(9, Font.NORMAL, TEXT)),
                    background, Element.ALIGN_CENTER, theme));

            final LocalDate expiration = toLocalDate(vaccine.getExpirationDate());
            final Color expirationColor = TEXT;
            table.addCell(bodyCell(new Phrase(formatDate(vaccine.getExpirationDate()),
                    font(9, Font.NORMAL, expirationColor)), background, Element.ALIGN_CENTER, theme));

            table.addCell(bodyCell(new Phrase(""), background, Element.ALIGN_LEFT, theme));
        }

        return table;
    }

    private Phrase vaccineNameAndDescription(final VaccineModel vaccine) {
        final Paragraph content = new Paragraph();
        content.setLeading(11f);
        content.add(new Chunk(safe(vaccine.getName()), font(10, Font.BOLD, TEXT)));

        final Object description = vaccine.getDescription();
        if (description != null && !String.valueOf(description).isBlank()) {
            content.add(Chunk.NEWLINE);
            content.add(new Chunk(String.valueOf(description).trim(), font(8, Font.NORMAL, MUTED)));
        }
        return content;
    }

    private PdfPCell headerCell(final String text, final Theme theme) {
        final PdfPCell cell = new PdfPCell(new Phrase(text, font(9, Font.BOLD, Color.WHITE)));
        cell.setBackgroundColor(theme.primary);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(8f);
        return cell;
    }

    private PdfPCell bodyCell(final Phrase content, final Color background, final int horizontalAlignment,
                              final Theme theme) {
        final PdfPCell cell = new PdfPCell(content);
        cell.setBorder(Rectangle.BOTTOM);
        cell.setBorderColorBottom(theme.line);
        cell.setBorderWidthBottom(0.6f);
        cell.setMinimumHeight(30f);
        cell.setPadding(6f);
        cell.setHorizontalAlignment(horizontalAlignment);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        if (background != null) {
            cell.setBackgroundColor(background);
        }
        return cell;
    }

    private AnimalModel findAnimal(final int animalId) {
        final PetModel pet = petService.findById(animalId);

        if (pet != null) {
            return pet;
        }

        return farmAnimalService.findById(animalId);
    }

    private String speciesLabel(final AnimalModel animal) {
        return animal.getSpecies() == null ? EMPTY : safe(animal.getSpecies().getName());
    }

    private String genderLabel(final String gender) {
        if ("M".equalsIgnoreCase(gender)) {
            return "Macho";
        }
        if ("F".equalsIgnoreCase(gender)) {
            return "Fêmea";
        }
        return EMPTY;
    }

    private String displayName(final AnimalModel animal) {
        if (animal.getName() != null && !animal.getName().isBlank()) {
            return animal.getName().trim();
        }

        if (animal instanceof FarmAnimalModel) {
            final String identify = ((FarmAnimalModel) animal).getIdentify();
            if (identify != null && !identify.isBlank()) {
                return identify.trim();
            }
        }

        return "Sem nome";
    }

    private String fileSlug(final String text) {
        final String withoutAccents = Normalizer.normalize(text, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
        final String slug = withoutAccents.replaceAll("[^A-Za-z0-9]+", "-")
                .replaceAll("^-+|-+$", "")
                .toLowerCase(Locale.ROOT);
        return slug.isEmpty() ? "animal" : slug;
    }

    private String ageLabel(final Object birthDate) {
        final LocalDate birth = toLocalDate(birthDate);
        final LocalDate today = LocalDate.now();

        if (birth == null || birth.isAfter(today)) {
            return EMPTY;
        }

        final Period period = Period.between(birth, today);

        if (period.getYears() > 0) {
            return period.getYears() + (period.getYears() == 1 ? " ano" : " anos");
        }
        if (period.getMonths() > 0) {
            return period.getMonths() + (period.getMonths() == 1 ? " mês" : " meses");
        }

        final long days = ChronoUnit.DAYS.between(birth, today);
        return days + (days == 1 ? " dia" : " dias");
    }

    private String formatWeight(final double weight) {
        return String.format(Locale.forLanguageTag("pt-BR"), "%.1f kg", weight);
    }

    private List<VaccineModel> sortByApplicationDate(final List<VaccineModel> vaccines) {
        final List<VaccineModel> sorted = new ArrayList<>();

        if (vaccines != null) {
            sorted.addAll(vaccines);
        }

        sorted.sort((first, second) -> {
            final LocalDate firstDate = toLocalDate(first.getApplicationDate());
            final LocalDate secondDate = toLocalDate(second.getApplicationDate());

            if (firstDate == null && secondDate == null) {
                return 0;
            }
            if (firstDate == null) {
                return 1;
            }
            if (secondDate == null) {
                return -1;
            }
            return firstDate.compareTo(secondDate);
        });

        return sorted;
    }

    private String truncate(final String text, final int maxLength) {
        return text.length() <= maxLength ? text : text.substring(0, maxLength - 3) + "...";
    }

    private String safe(final Object value) {
        if (value == null) {
            return EMPTY;
        }
        final String text = String.valueOf(value).trim();
        return text.isEmpty() ? EMPTY : text;
    }

    private String formatDate(final Object value) {
        if (value == null) {
            return EMPTY;
        }
        final LocalDate date = toLocalDate(value);
        return date == null ? safe(value) : date.format(DATE_FORMAT);
    }

    private LocalDate toLocalDate(final Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof LocalDate) {
            return (LocalDate) value;
        }
        if (value instanceof LocalDateTime) {
            return ((LocalDateTime) value).toLocalDate();
        }
        if (value instanceof java.sql.Date) {
            return ((java.sql.Date) value).toLocalDate();
        }
        if (value instanceof java.util.Date) {
            return ((java.util.Date) value).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        }

        final String text = String.valueOf(value).trim();
        if (text.length() >= 10) {
            try {
                return LocalDate.parse(text.substring(0, 10));
            } catch (final DateTimeParseException ignored) {
            }
        }
        return null;
    }

    private static Font font(final float size, final int style, final Color color) {
        return new Font(Font.HELVETICA, size, style, color);
    }

    private static Color mix(final Color base, final Color other, final float ratio) {
        return new Color(
                Math.round(base.getRed() + (other.getRed() - base.getRed()) * ratio),
                Math.round(base.getGreen() + (other.getGreen() - base.getGreen()) * ratio),
                Math.round(base.getBlue() + (other.getBlue() - base.getBlue()) * ratio));
    }

    private Image loadImage(final String classpathLocation) throws IOException {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(classpathLocation)) {
            if (inputStream == null) {
                throw new IOException("Imagem não encontrada no classpath: " + classpathLocation);
            }
            final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            inputStream.transferTo(buffer);
            return Image.getInstance(buffer.toByteArray());
        }
    }


    private static final class Theme {

        private final Color primary;
        private final Color primaryDark;
        private final Color softBg;
        private final Color zebra;
        private final Color line;
        private final Color headerSubtitle;

        private Theme(final Color primary) {
            this.primary = primary;
            this.primaryDark = mix(primary, Color.BLACK, 0.30f);
            this.softBg = mix(primary, Color.WHITE, 0.92f);
            this.zebra = mix(primary, Color.WHITE, 0.95f);
            this.line = mix(primary, Color.WHITE, 0.78f);
            this.headerSubtitle = mix(primary, Color.WHITE, 0.82f);
        }
    }

    private static class RoundedBoxEvent implements PdfPCellEvent {

        private final Color fill;
        private final Color stroke;

        private RoundedBoxEvent(final Color fill, final Color stroke) {
            this.fill = fill;
            this.stroke = stroke;
        }

        @Override
        public void cellLayout(final PdfPCell cell, final Rectangle position, final PdfContentByte[] canvases) {
            final PdfContentByte cb = canvases[PdfPTable.BACKGROUNDCANVAS];
            cb.saveState();
            if (fill != null) {
                cb.setColorFill(fill);
            }
            cb.setColorStroke(stroke);
            cb.setLineWidth(0.9f);
            cb.roundRectangle(position.getLeft(), position.getBottom(), position.getWidth(), position.getHeight(), 10f);
            if (fill != null) {
                cb.fillStroke();
            } else {
                cb.stroke();
            }
            cb.restoreState();
        }
    }

    private static class VaccinationCardPageEvent extends PdfPageEventHelper {

        private final Theme theme;
        private final Image logoImage;
        private final String animalLabel;
        private final String issuedAt;

        private VaccinationCardPageEvent(final Theme theme, final Image logoImage,
                                         final String animalLabel, final String issuedAt) {
            this.theme = theme;
            this.logoImage = logoImage;
            this.animalLabel = animalLabel;
            this.issuedAt = issuedAt;
        }

        @Override
        public void onEndPage(final PdfWriter writer, final Document document) {
            final Rectangle page = document.getPageSize();
            final PdfContentByte under = writer.getDirectContentUnder();
            final PdfContentByte over = writer.getDirectContent();

            try {
                drawWatermarkLogo(under, page);
                drawHeader(under, over, page);
            } catch (final DocumentException e) {
                throw new RuntimeException("Erro ao desenhar o cartão de vacinação", e);
            }

            drawFooter(over, page, writer.getPageNumber());
        }

        private void drawLogo(final PdfContentByte canvas, final float centerX, final float centerY,
                              final float maxWidth, final float maxHeight) throws DocumentException {
            logoImage.scaleToFit(maxWidth, maxHeight);
            logoImage.setAbsolutePosition(
                    centerX - logoImage.getScaledWidth() / 2,
                    centerY - logoImage.getScaledHeight() / 2);
            canvas.addImage(logoImage);
        }

        private void drawWatermarkLogo(final PdfContentByte under, final Rectangle page) throws DocumentException {
            final PdfGState opacity = new PdfGState();
            opacity.setFillOpacity(0.06f);

            under.saveState();
            under.setGState(opacity);
            drawLogo(under, page.getWidth() / 2, page.getHeight() / 2 - 40f, 340f, 340f);
            under.restoreState();
        }

        private void drawHeader(final PdfContentByte under, final PdfContentByte over, final Rectangle page)
                throws DocumentException {
            final float width = page.getWidth();
            final float height = page.getHeight();

            under.saveState();
            under.setColorFill(theme.primary);
            under.rectangle(0, height - HEADER_HEIGHT, width, HEADER_HEIGHT);
            under.fill();
            under.setColorFill(ACCENT);
            under.rectangle(0, height - HEADER_HEIGHT - 4f, width, 4f);
            under.fill();
            under.restoreState();

            final PdfGState soft = new PdfGState();
            soft.setFillOpacity(0.08f);
            under.saveState();
            under.setGState(soft);
            under.setColorFill(Color.WHITE);
            under.circle(width - 40f, height - 10f, 90f);
            under.fill();
            under.restoreState();

            final float badgeRadius = 35f;
            final float badgeX = width - PAGE_MARGIN - badgeRadius;
            final float badgeY = height - HEADER_HEIGHT / 2;

            under.saveState();
            under.setColorFill(Color.WHITE);
            under.setColorStroke(ACCENT);
            under.setLineWidth(2f);
            under.circle(badgeX, badgeY, badgeRadius);
            under.fillStroke();
            under.restoreState();

            drawLogo(under, badgeX, badgeY, 42f, 42f);

            ColumnText.showTextAligned(over, Element.ALIGN_LEFT,
                    new Phrase("PETLINK", font(8, Font.BOLD, new Color(0xFF, 0xE3, 0xAE))),
                    PAGE_MARGIN, height - 30f, 0);
            ColumnText.showTextAligned(over, Element.ALIGN_LEFT,
                    new Phrase("CARTEIRA DE VACINAÇÃO", font(24, Font.BOLD, Color.WHITE)),
                    PAGE_MARGIN, height - 58f, 0);
            ColumnText.showTextAligned(over, Element.ALIGN_LEFT,
                    new Phrase("Registro de imunização do animal: " + animalLabel,
                            font(11, Font.NORMAL, theme.headerSubtitle)),
                    PAGE_MARGIN, height - 80f, 0);
        }

        private void drawFooter(final PdfContentByte over, final Rectangle page, final int pageNumber) {
            over.saveState();
            over.setColorStroke(theme.line);
            over.setLineWidth(0.8f);
            over.moveTo(PAGE_MARGIN, 42f);
            over.lineTo(page.getWidth() - PAGE_MARGIN, 42f);
            over.stroke();
            over.restoreState();

            ColumnText.showTextAligned(over, Element.ALIGN_LEFT,
                    new Phrase("Emitido pelo PetLink em " + issuedAt, font(8, Font.NORMAL, MUTED)),
                    PAGE_MARGIN, 28f, 0);
            ColumnText.showTextAligned(over, Element.ALIGN_RIGHT,
                    new Phrase("Página " + pageNumber, font(8, Font.NORMAL, MUTED)),
                    page.getWidth() - PAGE_MARGIN, 28f, 0);
        }
    }
}