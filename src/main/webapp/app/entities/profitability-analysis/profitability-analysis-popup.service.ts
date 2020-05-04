import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { ProfitabilityAnalysis } from './profitability-analysis.model';
import { ProfitabilityAnalysisService } from './profitability-analysis.service';

@Injectable()
export class ProfitabilityAnalysisPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private profitabilityAnalysisService: ProfitabilityAnalysisService

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
                this.profitabilityAnalysisService.find(id).subscribe((profitabilityAnalysis) => {
                    if (profitabilityAnalysis.date) {
                        profitabilityAnalysis.date = {
                            year: profitabilityAnalysis.date.getFullYear(),
                            month: profitabilityAnalysis.date.getMonth() + 1,
                            day: profitabilityAnalysis.date.getDate()
                        };
                    }
                    this.ngbModalRef = this.profitabilityAnalysisModalRef(component, profitabilityAnalysis);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.profitabilityAnalysisModalRef(component, new ProfitabilityAnalysis());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    profitabilityAnalysisModalRef(component: Component, profitabilityAnalysis: ProfitabilityAnalysis): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.profitabilityAnalysis = profitabilityAnalysis;
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
