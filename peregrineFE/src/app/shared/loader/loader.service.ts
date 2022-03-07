import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class LoaderService {

  // Loader Data
  private loaderBS = new BehaviorSubject<boolean>(false);
  private readonly loaderObs = this.loaderBS.asObservable();

  /**
   * Set loader data
   * @param loader the loader data
   */
  set loader(loader: boolean) {
    this.loaderBS.next(loader)
  }

  /**
   * Get loader data
   * @returns the loader data
   */
  get loader(): boolean { return this.loaderBS.value; }

  /**
   * Get the loader data as observable
   * @returns Observale with loader data
   */
  getLoaderObs(): Observable<boolean> { return this.loaderObs; }
}
