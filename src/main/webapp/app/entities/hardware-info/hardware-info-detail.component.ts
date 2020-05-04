import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IHardwareInfo } from 'app/shared/model/hardware-info.model';

@Component({
  selector: 'jhi-hardware-info-detail',
  templateUrl: './hardware-info-detail.component.html'
})
export class HardwareInfoDetailComponent implements OnInit {
  hardwareInfo: IHardwareInfo | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ hardwareInfo }) => (this.hardwareInfo = hardwareInfo));
  }

  previousState(): void {
    window.history.back();
  }
}
