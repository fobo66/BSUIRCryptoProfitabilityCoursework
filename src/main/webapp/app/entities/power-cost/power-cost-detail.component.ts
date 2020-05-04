import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPowerCost } from 'app/shared/model/power-cost.model';

@Component({
  selector: 'jhi-power-cost-detail',
  templateUrl: './power-cost-detail.component.html'
})
export class PowerCostDetailComponent implements OnInit {
  powerCost: IPowerCost | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ powerCost }) => (this.powerCost = powerCost));
  }

  previousState(): void {
    window.history.back();
  }
}
