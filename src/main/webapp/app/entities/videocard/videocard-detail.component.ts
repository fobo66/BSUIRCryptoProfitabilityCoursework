import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IVideocard } from 'app/shared/model/videocard.model';

@Component({
  selector: 'jhi-videocard-detail',
  templateUrl: './videocard-detail.component.html'
})
export class VideocardDetailComponent implements OnInit {
  videocard: IVideocard | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ videocard }) => (this.videocard = videocard));
  }

  previousState(): void {
    window.history.back();
  }
}
