import { TestBed } from '@angular/core/testing';

import { ModsApiService } from './mods-api-service';

describe('ModsApiService', () => {
  let service: ModsApiService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ModsApiService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
