import { TestBed } from '@angular/core/testing';
import { take } from 'rxjs';
import { Alert } from './alert.component';

import { AlertService } from './alert.service';

describe('AlertService', () => {
  let service: AlertService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AlertService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should be {} initially and set alert as value and recieve from getter and observable', async () => {
    expect(service.alert).toEqual({});
    service.getAlertObs().pipe(take(1)).subscribe(data => {
      expect(data).toEqual({});
    });
    const setValue: Alert = {
      type: 'red',
      message: 'message',
      showAlert: true,
      autoCloseTime: 2000
    };
    service.alert = setValue;
    expect(service.alert).toEqual(setValue);
    service.getAlertObs().subscribe(data => {
      expect(data).toEqual(setValue);
    });
  });

  it('should have function alertMessage defined', () => {
    expect(service.alertMessage).toBeDefined();
  });

  it('should update alert variable via alertMessage and its parameter accordingly', () => {
    expect(service.alertMessage).toBeDefined();
    service.alertMessage('message1');
    let matchvalue: Alert = {
      type: 'red',
      message: 'message1',
      showAlert: true,
      autoCloseTime: 2000
    };
    expect(service.alert).toEqual(matchvalue);
    service.getAlertObs().subscribe(data => expect(data).toEqual(matchvalue));
    matchvalue = {
      type: 'green',
      message: 'message2',
      showAlert: true,
      autoCloseTime: 2000
    };
    service.alertMessage('message2', 'green');
    expect(service.alert).toEqual(matchvalue);
    service.getAlertObs().subscribe(data => expect(data).toEqual(matchvalue));
    matchvalue = {
      type: 'green',
      message: 'message3',
      showAlert: true,
      autoCloseTime: 3000
    };
    service.alertMessage('message3', 'green', 3000);
    expect(service.alert).toEqual(matchvalue);
    service.getAlertObs().subscribe(data => expect(data).toEqual(matchvalue));
  });
});
