import {Component, EventEmitter, OnDestroy, OnInit, Output} from '@angular/core';
import {AdvertModel} from './advert/advert.model';
import {AdvertService} from './advert.service';
import {Subscription} from 'rxjs/Subscription';
import {DataStorageService} from '../shared/data-storage.service';

@Component({
  selector: 'app-adverts',
  templateUrl: './advert-list.component.html',
  styleUrls: ['./advert-list.component.css']
})
export class AdvertListComponent implements OnInit, OnDestroy {
  private advertSubscription: Subscription;

  adverts: AdvertModel[];

  constructor(private advertService: AdvertService,
              private dataStorageService: DataStorageService) {
  }

  ngOnInit() {
    this.dataStorageService.getAdverts();
    this.adverts = this.advertService.getAdverts();
    this.advertSubscription = this.advertService.advertsChanged
      .subscribe(
        (adverts: AdvertModel[]) => {
          this.adverts = adverts;
        }
      );
  }

  ngOnDestroy() {
    this.advertSubscription.unsubscribe();
  }

}
