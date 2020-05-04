import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { Cryptocurrency } from './cryptocurrency.model';
import { CryptocurrencyService } from './cryptocurrency.service';

@Injectable()
export class CryptocurrencyPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private cryptocurrencyService: CryptocurrencyService

    ) {
        this.ngbModalRef = null;
    }

    open(component: Component, id?: number | any): Promise<NgbModalRef> {
        return new Promise<NgbModalRef>((resolve, reject) => {
            const isOpen = this.ngbModalRef !== null;
            if (isOpen) {
                resolve(this.ngbModalRef);
            }

            if (id) {
                this.cryptocurrencyService.find(id).subscribe((cryptocurrency) => {
                    this.ngbModalRef = this.cryptocurrencyModalRef(component, cryptocurrency);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.cryptocurrencyModalRef(component, new Cryptocurrency());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    cryptocurrencyModalRef(component: Component, cryptocurrency: Cryptocurrency): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.cryptocurrency = cryptocurrency;
        modalRef.result.then((result) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.ngbModalRef = null;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.ngbModalRef = null;
        });
        return modalRef;
    }
}
