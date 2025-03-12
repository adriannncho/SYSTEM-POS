import { Injectable } from "@angular/core";
import { NzMessageService } from "ng-zorro-antd/message";
import { NzNotificationService } from "ng-zorro-antd/notification";


@Injectable({
  providedIn: 'root'
})

export class NotificationService {
  
  constructor(
    private nzNotification: NzNotificationService,
    private nzMessage: NzMessageService
  ){}

  private notify(title: string, message: string, icon: string, messageType: boolean, backgroundColor: string, fontColor:string, time: number, type : string): void {
    if (messageType) this.nzMessage.create(icon, message);
    else this.nzNotification.create(icon, "", message, {
        nzStyle: {
        'background-color': backgroundColor,
        'color': fontColor,
        'font-color': fontColor,
        'padding-right': '60px'
      },
       nzDuration: time});

  }

  remove(){
    this.nzNotification.remove();
  }

  success(message: string, title: string = '', messageType: boolean = false, time: number = 6000,): void {
    this.notify(title, message, 'success', messageType, '#D7F0D9', '#155724', time, 'success');
  }

  info(message: string, title: string = '', messageType: boolean = false, time: number = 6000,): void {
    this.notify(title, message, 'info', messageType, '#CFDCF3', '#004175', time, 'info');
  }

  warning(message: string, title: string = '', messageType: boolean = false, time: number = 6000,): void {
    this.notify(title, message, 'warning', messageType, '#FCF5CB', '#866404', time, 'warning');
  }

  error(message: string, title: string = '', error: any = null, time: number = 6000): void {
    if (error && error.error && error.error.detail) {
      message = error.error.detail;
      if (error.error.detail.split(';')[1] && error.error.detail.split(';')[1].length >= 1){
        let errors: string[];
        message="";
        errors = error.error.detail.split(';');
        errors.forEach(err => {
          message += err + "." + "<br />";
        });
      }
      this.notify(title, message, 'error', false, '#F7CAC8', '#721C24', time, 'error');
    } else {
      // this.notify(title, message, 'error', false, '#F7CAC8', '#721C24', time);
      this.notify(title, message, 'error', false, '#F7CAC8', '#721C24', time, 'error');
    }
  }
}