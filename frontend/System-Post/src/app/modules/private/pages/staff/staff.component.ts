import { Component, OnInit } from '@angular/core';
import { NG_ZORRO_MODULES } from '../../../../shared/config/ng-zorro.config';
import { NotificationService } from '../../../../core/services/notification/notification.service';
import { StaffService } from '../../../../services/staff/staff.services';
import { User } from '../../../../interfaces/staff/staff.interface';

@Component({
  selector: 'app-staff',
  standalone: true,
  imports: [
    ...NG_ZORRO_MODULES,
  ],
  templateUrl: './staff.component.html',
  styleUrl: './staff.component.css'
})
export class StaffComponent implements OnInit{

  loadingUser: boolean = false;
  userResponse: User[] = [];

  listOfCurrentPageData: readonly User[] = [];
  checked = false;
  indeterminate = false;
  setOfCheckedId = new Set<number>();

  constructor(
    private staffService: StaffService,
    private notificationService: NotificationService
  ) {

  }

  ngOnInit(): void {
    this.getUsers;
  }

  /**
   * Obtiene los usuarios existentes
   */
  getUsers(): void {
    this.loadingUser = true;
    this.staffService.getStaff().subscribe(users => {
      if (users && users.length > 0) {
        this.userResponse = users;
      } else {
        this.notificationService.error("No se encontraron usuarios disponibles", "Cargar usuarios");
      }
      this.loadingUser = false;
    }, (error => {
      this.loadingUser = false;
      this.notificationService.error("Ocurrio un erro al cargar los usuarios disponibles", "Cargar usuarios")
    }));
  }

  updateCheckedSet(id: number, checked: boolean): void {
    if (checked) {
      this.setOfCheckedId.add(id);
    } else {
      this.setOfCheckedId.delete(id);
    }
  }

  onItemChecked(id: number, checked: boolean): void {
    this.updateCheckedSet(id, checked);
    this.refreshCheckedStatus();
  }

  onAllChecked(value: boolean): void {
    this.listOfCurrentPageData.forEach(item => this.updateCheckedSet(item.documentNumber, value));
    this.refreshCheckedStatus();
  }

  onCurrentPageDataChange($event: readonly User[]): void {
    this.listOfCurrentPageData = $event;
    this.refreshCheckedStatus();
  }

  refreshCheckedStatus(): void {
    this.checked = this.listOfCurrentPageData.every(item => this.setOfCheckedId.has(item.documentNumber));
    this.indeterminate = this.listOfCurrentPageData.some(item => this.setOfCheckedId.has(item.documentNumber)) && !this.checked;
  }
}
