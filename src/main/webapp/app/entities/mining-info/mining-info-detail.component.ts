import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMiningInfo } from 'app/shared/model/mining-info.model';

@Component({
  selector: 'jhi-mining-info-detail',
  templateUrl: './mining-info-detail.component.html'
})
export class MiningInfoDetailComponent implements OnInit {
  miningInfo: IMiningInfo | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ miningInfo }) => (this.miningInfo = miningInfo));
  }

  previousState(): void {
    window.history.back();
  }
}
