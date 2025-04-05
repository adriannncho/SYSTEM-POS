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

}
