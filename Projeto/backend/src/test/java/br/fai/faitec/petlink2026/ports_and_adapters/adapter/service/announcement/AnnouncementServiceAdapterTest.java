package br.fai.faitec.petlink2026.ports_and_adapters.adapter.service.announcement;

import br.fai.faitec.petlink2026.domain.announcement.AnnouncementModel;
import br.fai.faitec.petlink2026.domain.announcement.AnnouncementType;
import br.fai.faitec.petlink2026.domain.user.AccountType;
import br.fai.faitec.petlink2026.domain.user.UserModel;
import br.fai.faitec.petlink2026.dto.announcement.ReadAnnouncementDto;
import br.fai.faitec.petlink2026.ports_and_adapters.port.dao.announcement.AnnouncementDao;
import br.fai.faitec.petlink2026.ports_and_adapters.port.service.user.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AnnouncementServiceAdapterTest {

    @Mock
    private AnnouncementDao announcementDao;
    @Mock
    private UserService userService;

    @InjectMocks
    private AnnouncementServiceAdapter announcementService;


    private static final AnnouncementType OTHER_TYPE = AnnouncementType.ADOPTION;

    @Test
    public void shouldRejectNegativeId() {
        assertTrue(announcementService.isIdInvalid(-1));
    }

    @Test
    public void shouldCreateLostAnnouncementWhenPersonHasDocument() {
        AnnouncementModel announcement = mock(AnnouncementModel.class);
        when(announcement.getTitle()).thenReturn("Cachorro perdido");
        when(announcement.getDescription()).thenReturn("Visto na praça central");
        when(announcement.getContact()).thenReturn("35 99999-9999");
        when(announcement.getIdCreator()).thenReturn(1);
        when(announcement.getAnnouncementType()).thenReturn(AnnouncementType.LOST);
        UserModel person = mock(UserModel.class);
        when(person.getAccountType()).thenReturn(AccountType.PERSON);
        when(person.getDocument()).thenReturn("12345678900");
        when(userService.findById(1)).thenReturn(person);
        when(announcementDao.add(announcement)).thenReturn(7);

        int result = announcementService.create(announcement);

        assertEquals(7, result);
        verify(announcementDao).add(announcement);
    }

    @Test
    public void shouldNotCreateWhenPersonPublishesNonLostAnnouncement() {
        AnnouncementModel announcement = mock(AnnouncementModel.class);
        when(announcement.getTitle()).thenReturn("Adoção de gatinhos");
        when(announcement.getDescription()).thenReturn("Gatinhos para adoção");
        when(announcement.getContact()).thenReturn("35 99999-9999");
        when(announcement.getIdCreator()).thenReturn(1);
        when(announcement.getAnnouncementType()).thenReturn(OTHER_TYPE);
        UserModel person = mock(UserModel.class);
        when(person.getAccountType()).thenReturn(AccountType.PERSON);
        when(userService.findById(1)).thenReturn(person);

        assertEquals(0, announcementService.create(announcement));
        verifyNoInteractions(announcementDao);
    }

    @Test
    public void shouldDeleteWhenAnnouncementExists() {
        AnnouncementModel announcement = mock(AnnouncementModel.class);
        when(announcementDao.readyById(1)).thenReturn(announcement);

        announcementService.delete(1);

        verify(announcementDao).remove(1);
    }

    @Test
    public void shouldNotDeleteWhenIdIsNegative() {
        announcementService.delete(-1);

        verifyNoInteractions(announcementDao);
    }

    @Test
    public void shouldFindAnnouncementById() {
        AnnouncementModel announcement = mock(AnnouncementModel.class);
        when(announcementDao.readyById(1)).thenReturn(announcement);

        assertSame(announcement, announcementService.findById(1));
    }

    @Test
    public void shouldReturnNullWhenAnnouncementDoesNotExist() {
        assertNull(announcementService.findById(99));
        verify(announcementDao).readyById(99);
    }

    @Test
    public void shouldReturnOnlyAnnouncementsFromUser() {
        UserModel user = mock(UserModel.class);
        AnnouncementModel own = mock(AnnouncementModel.class);
        AnnouncementModel other = mock(AnnouncementModel.class);
        when(userService.findById(1)).thenReturn(user);
        when(announcementDao.readAll()).thenReturn(List.of(own, other));
        when(own.getIdCreator()).thenReturn(1);
        when(other.getIdCreator()).thenReturn(2);

        List<AnnouncementModel> result = announcementService.showAllAnnouncementsByUserId(1);

        assertEquals(1, result.size());
        assertSame(own, result.get(0));
    }

    @Test
    public void shouldBuildDtoWithAnnouncementAndCreatorData() {
        AnnouncementModel announcement = mock(AnnouncementModel.class);
        UserModel creator = mock(UserModel.class);
        when(announcementDao.readyById(1)).thenReturn(announcement);
        when(announcement.getIdCreator()).thenReturn(10);
        when(userService.findById(10)).thenReturn(creator);

        ReadAnnouncementDto result = announcementService.getAnnouncementById(1);

        assertNotNull(result);
    }

    @Test
    public void shouldReturnNullDtoWhenCreatorDoesNotExist() {
        AnnouncementModel announcement = mock(AnnouncementModel.class);
        when(announcementDao.readyById(1)).thenReturn(announcement);
        when(announcement.getIdCreator()).thenReturn(10);
        when(userService.findById(10)).thenReturn(null);

        assertNull(announcementService.getAnnouncementById(1));
    }

    @Test
    public void shouldUpdateAnnouncementInformation() {
        AnnouncementModel existing = mock(AnnouncementModel.class);
        AnnouncementModel changes = mock(AnnouncementModel.class);
        when(announcementDao.readyById(1)).thenReturn(existing);
        when(changes.getTitle()).thenReturn("Novo título");
        when(changes.getDescription()).thenReturn("Nova descrição");

        boolean result = announcementService.update(1, changes);

        assertTrue(result);
        verify(existing).setTitle("Novo título");
        verify(existing).setDescription("Nova descrição");
        verify(announcementDao).updateInformation(1, existing);
    }

    @Test
    public void shouldNotUpdateWhenTitleIsEmpty() {
        AnnouncementModel existing = mock(AnnouncementModel.class);
        AnnouncementModel changes = mock(AnnouncementModel.class);
        when(announcementDao.readyById(1)).thenReturn(existing);
        when(changes.getTitle()).thenReturn("");

        assertFalse(announcementService.update(1, changes));
        verify(announcementDao, never()).updateInformation(1, existing);
    }
}