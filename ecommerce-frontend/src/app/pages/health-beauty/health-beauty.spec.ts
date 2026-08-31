import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HealthBeauty } from './health-beauty';

describe('HealthBeauty', () => {
  let component: HealthBeauty;
  let fixture: ComponentFixture<HealthBeauty>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HealthBeauty],
    }).compileComponents();

    fixture = TestBed.createComponent(HealthBeauty);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
