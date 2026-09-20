import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { Pet } from '../../../../models/domain/pet';
import { FarmAnimalSale, KG_PER_ARROBA, PriceType } from '../../../../models/domain/farmAnimalSale';
import { CurrentUserService } from '../../../../services/security/current-user';
import { SaleCreateService } from '../../../../services/sale/sale-create';
import { speciesIcon } from '../../../../shared/pet-species-icon';
import { SaleReadService } from '../../../../services/sale/sale-read';
import { FarmAnimalReadService } from '../../../../services/farm-animal/farm-animal-read';
import { SaleDeleteService } from '../../../../services/sale/sale-delete';


interface SaleListing {
    sale: FarmAnimalSale;
    animals: Pet[];
}

@Component({
    selector: 'app-sale-hub',
    imports: [MatIconModule, ReactiveFormsModule],
    templateUrl: './sale-hub.html',
    styleUrl: './sale-hub.css',
})
export class SaleHub implements OnInit {

    readonly PriceType = PriceType;

    activeTab: 'comprar' | 'vender' = 'comprar';
    loading: boolean = true;
    userId: number | null = null;
    mySales: SaleListing[] = [];
    selectedSale: SaleListing | null = null;
    confirmingDelete: boolean = false;
    saleDeleteFailed: boolean = false;
    sellableAnimals: Pet[] = [];
    availableSales: SaleListing[] = [];
    selectedAnimalIds: number[] = [];

    saleForm: FormGroup;
    saleValidationFailed: boolean = false;
    saleCreatedOk: boolean = false;

    constructor(
        private farmAnimalReadService: FarmAnimalReadService,
        private currentUserService: CurrentUserService,
        private saleCreateService: SaleCreateService,
        private saleReadService: SaleReadService,
        private saleDeleteService: SaleDeleteService,
        private formBuilder: FormBuilder,
        private toastrService: ToastrService,
        private cdr: ChangeDetectorRef,
    ) {
        this.saleForm = this.formBuilder.group({
            description: ['', [Validators.required]],
            contact: ['', [Validators.required]],
            priceType: [PriceType.MANUAL, [Validators.required]],
            pricePerArroba: [null],
            price: [null, [Validators.required, Validators.min(0.01)]],
        });
    }

    async ngOnInit(): Promise<void> {
        try {
            const currentUser = this.currentUserService.get() ?? await this.currentUserService.load();
            if (!currentUser?.id) {
                throw new Error('Usuário atual não encontrado');
            }
            this.userId = currentUser.id;
            const [myAnimals, allSales] = await Promise.all([
                this.farmAnimalReadService.findByOwnerId(this.userId),
                this.saleReadService.findAll(),
            ]);

            this.sellableAnimals = myAnimals.filter(animal => !animal.forSell);
            this.availableSales = await this.loadListings(allSales.filter(sale => sale.userId !== this.userId));
            this.mySales = await this.loadListings(allSales.filter(sale => sale.userId === this.userId));
        } catch (error) {
            console.error('Erro ao carregar animais para venda', error);
        } finally {
            this.loading = false;
            this.cdr.detectChanges();
        }
    }

    private async loadListings(sales: FarmAnimalSale[]): Promise<SaleListing[]> {
        const listings = await Promise.all(
            sales.map(async sale => {
                try {
                    const animals = await Promise.all(
                        sale.farmAnimalIds.map(id => this.farmAnimalReadService.findById(String(id)))
                    );
                    return { sale, animals };
                } catch (error) {
                    console.error(`Erro ao buscar animais da venda ${sale.id}`, error);
                    return null;
                }
            })
        );

        return listings.filter((listing): listing is SaleListing => listing !== null);
    }

    priceTypeLabel(sale: FarmAnimalSale): string {
        return sale.priceType === PriceType.PER_ARROBA
            ? 'Por arroba: R$ ' + (sale.pricePerArroba ?? 0).toFixed(2)
            : 'Valor fechado';
    }

    listingWeight(listing: SaleListing): number {
        return listing.animals.reduce((sum, animal) => sum + (animal.weight ?? 0), 0);
    }

    speciesIcon(species: string): string {
        return speciesIcon(species);
    }

    setTab(tab: 'comprar' | 'vender'): void {
        this.activeTab = tab;
    }

    isSelected(id: number | undefined): boolean {
        return id !== undefined && this.selectedAnimalIds.includes(id);
    }

    toggleAnimal(id: number | undefined): void {
        if (id === undefined) {
            return;
        }
        this.selectedAnimalIds = this.selectedAnimalIds.includes(id)
            ? this.selectedAnimalIds.filter(selectedId => selectedId !== id)
            : [...this.selectedAnimalIds, id];
    }

    get selectedPriceType(): PriceType {
        return this.saleForm.controls['priceType'].value;
    }

    get totalWeight(): number {
        return this.sellableAnimals
            .filter(animal => this.isSelected(animal.id))
            .reduce((sum, animal) => sum + (animal.weight ?? 0), 0);
    }

    get totalArrobas(): number {
        return this.totalWeight / KG_PER_ARROBA;
    }

    get suggestedPrice(): number | null {
        const pricePerArroba = Number(this.saleForm.controls['pricePerArroba'].value);
        if (this.selectedPriceType !== PriceType.PER_ARROBA || !(pricePerArroba > 0) || this.totalWeight <= 0) {
            return null;
        }
        return this.totalArrobas * pricePerArroba;
    }

    applySuggestedPrice(): void {
        const suggested = this.suggestedPrice;
        if (suggested !== null) {
            this.saleForm.patchValue({ price: Math.round(suggested * 100) / 100 });
        }
    }

    validateSaleFields(): boolean {
        if (!this.saleForm.valid || this.selectedAnimalIds.length === 0) {
            return false;
        }
        if (this.selectedPriceType === PriceType.PER_ARROBA) {
            return Number(this.saleForm.controls['pricePerArroba'].value) > 0;
        }
        return true;
    }

    saleTitle(listing: SaleListing): string {
        return listing.animals.length > 1
            ? 'Lote de ' + listing.animals.length + ' animais'
            : (listing.animals[0]?.name || 'Animal');
    }

    openSale(listing: SaleListing): void {
        this.selectedSale = listing;
        this.confirmingDelete = false;
        this.saleDeleteFailed = false;
    }

    closeSale(): void {
        this.selectedSale = null;
        this.confirmingDelete = false;
    }

    requestDeleteSale(): void {
        this.confirmingDelete = true;
        this.saleDeleteFailed = false;
    }

    cancelDeleteSale(): void {
        this.confirmingDelete = false;
    }

    confirmDeleteSale(): void {
        const saleId = this.selectedSale?.sale.id;
        if (saleId === undefined) {
            return;
        }

        this.saleDeleteService.delete(saleId).subscribe({
            next: () => {
                this.closeSale();
                this.toastrService.success('Venda cancelada.');
                void this.ngOnInit();
            },
            error: (error) => {
                console.error('Erro ao excluir venda', error);
                this.saleDeleteFailed = true;
                this.cdr.detectChanges();
            },
        });
    }

    createSale(): void {
        this.saleValidationFailed = false;
        this.saleCreatedOk = false;

        if (!this.userId || !this.validateSaleFields()) {
            this.saleValidationFailed = true;
            return;
        }

        const isPerArroba = this.selectedPriceType === PriceType.PER_ARROBA;

        const sale: FarmAnimalSale = {
            description: this.saleForm.controls['description'].value,
            farmAnimalIds: this.selectedAnimalIds,
            priceType: this.selectedPriceType,
            pricePerArroba: isPerArroba ? Number(this.saleForm.controls['pricePerArroba'].value) : null,
            price: Number(this.saleForm.controls['price'].value),
            userId: this.userId,
            contact: this.saleForm.controls['contact'].value,
        };

        this.saleCreateService.create(sale).subscribe({
            next: () => {
                this.selectedAnimalIds = [];
                this.saleForm.reset({ priceType: PriceType.MANUAL });
                this.saleCreatedOk = true;
                this.toastrService.success('Venda publicada com sucesso!');
                void this.ngOnInit();
            },
            error: (error) => {
                console.error('Erro ao publicar venda', error);
                this.saleValidationFailed = true;
                this.cdr.detectChanges();
            },
        });
    }
}