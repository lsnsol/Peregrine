import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { AlertColor } from 'src/app/models/general.model';
import { LoaderService } from '../loader/loader.service';
import { Alert } from './alert.component';

@Injectable({
  providedIn: 'root'
})
export class AlertService {

  constructor(private loaderService: LoaderService) { }

  // Alert Data
  private alertBS = new BehaviorSubject<Alert>({});
  private readonly alertObs = this.alertBS.asObservable();

  /**
   * Set alert data
   * @param alert the alert data
   */
  set alert(alert: Alert) {
    this.alertBS.next(alert)
  }

  /**
   * Get alert data
   * @returns the alert data
   */
  get alert(): Alert { return this.alertBS.value; }

  /**
   * Get the alert data as observable
   * @returns Observale with alert data
   */
  getAlertObs(): Observable<Alert> { return this.alertObs; }

  /**
   * Construct and display alert messages in the applicaiton
   * @param message message to show
   * @param type type of alert
   * @param autoCloseTime auto close time of the alert
   */
  public alertMessage(message: string = 'Server Error', type: AlertColor = 'red', autoCloseTime: number = 2000): void {
    this.loaderService.loader = false;
    this.alert = {
      type,
      message,
      showAlert: true,
      autoCloseTime
    };
  }
}
