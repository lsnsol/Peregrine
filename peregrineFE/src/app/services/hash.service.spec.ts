import { TestBed } from '@angular/core/testing';

import { HashService } from './hash.service';

describe('HashService', () => {

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [HashService]
    });
  });

  it('should be created', () => {
    expect(HashService).toBeTruthy();
  });

  it('should have hash function', () => {
    expect(HashService.hash).toBeDefined();
    const testString = 'test';
    const getValue = HashService.hash(testString);
    const matchValue = '9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08';
    expect(getValue).toBe(matchValue);
  });
});
