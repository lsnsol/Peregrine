import { Component, Input, OnChanges, OnInit, ViewChild } from '@angular/core';
import { NgbAlert, NgbAlertConfig } from '@ng-bootstrap/ng-bootstrap';
import { debounceTime, Subject } from 'rxjs';
import { AlertColor, DateTypes } from 'src/app/models/general.model';

export interface Alert {
  type?: AlertColor;
  message?: string;
  animation?: boolean;
  dismissible?: boolean;
  autoCloseTime?: number;
  showAlert?: boolean;
  showDate?: boolean;
  dateFormat?: DateTypes;
}

@Component({
  selector: 'peregrine-alert',
  templateUrl: './alert.component.html',
  styleUrls: ['./alert.component.scss'],
  providers: [NgbAlertConfig]
})
export class AlertComponent implements OnInit, OnChanges {
  @ViewChild('alert', { static: false }) alert?: NgbAlert;
  @Input() type: AlertColor = 'neutral';
  @Input() message: string = 'Alert';
  @Input() animation: boolean = true;
  @Input() dismissible: boolean = false;
  @Input() autoCloseTime: number = 5000;
  @Input() showAlert: boolean = false;
  @Input() showDate: boolean = false;
  @Input() dateFormat: DateTypes = 'mediumDate';

  private _success = new Subject<string>();
  public _date: Date = new Date();

  constructor(private alertConfig: NgbAlertConfig) { }

  ngOnInit(): void {
    this._success.subscribe((message: string) => this.message = message);
  }

  ngOnChanges(): void {
    if (this.type === 'green')
      this.alertConfig.type = 'success';
    else if (this.type === 'blue')
      this.alertConfig.type = 'info';
    else if (this.type === 'yellow')
      this.alertConfig.type = 'warning';
    else if (this.type === 'red')
      this.alertConfig.type = 'danger';
    else if (this.type === 'neutral')
      this.alertConfig.type = 'dark';
    this.alertConfig.animation = this.animation;
    this.alertConfig.dismissible = false;
    this.triggerAlert();
  }

  /**
   * triggering alert to show on view
   */
  public triggerAlert(): void {
    if (this.showDate) {
      this._date = new Date();
    }
    this._success.pipe(debounceTime(this.autoCloseTime ? this.autoCloseTime : 5000)).subscribe(() => {
      if (this.alert) {
        this.alert.close();
      }
    });
    this._success.next(this.message);
  }
}