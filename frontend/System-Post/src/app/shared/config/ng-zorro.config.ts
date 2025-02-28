import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { NzButtonModule } from 'ng-zorro-antd/button';
import { NzCheckboxModule } from 'ng-zorro-antd/checkbox';
import { NzAffixModule } from 'ng-zorro-antd/affix';
import { NzAlertModule } from 'ng-zorro-antd/alert';
import { NzAnchorModule } from 'ng-zorro-antd/anchor';
import { NzAvatarModule } from 'ng-zorro-antd/avatar';
import { NzBadgeModule } from 'ng-zorro-antd/badge';
import { NzBreadCrumbModule } from 'ng-zorro-antd/breadcrumb';
import { NzCardModule } from 'ng-zorro-antd/card';
import { NzCarouselModule } from 'ng-zorro-antd/carousel';
import { NzCollapseModule } from 'ng-zorro-antd/collapse';
import { NzDatePickerModule } from 'ng-zorro-antd/date-picker';
import { NzDividerModule } from 'ng-zorro-antd/divider';
import { NzDropDownModule } from 'ng-zorro-antd/dropdown';
import { NzEmptyModule } from 'ng-zorro-antd/empty';
import { NzFormModule } from 'ng-zorro-antd/form';
import { NzI18nModule } from 'ng-zorro-antd/i18n';
import { NzIconModule } from 'ng-zorro-antd/icon';
import { NzImageModule } from 'ng-zorro-antd/image';
import { NzInputModule } from 'ng-zorro-antd/input';
import { NzInputNumberModule } from 'ng-zorro-antd/input-number';
import { NzLayoutModule } from 'ng-zorro-antd/layout';
import { NzListModule } from 'ng-zorro-antd/list';
import { NzMenuModule } from 'ng-zorro-antd/menu';
import { NzMessageModule } from 'ng-zorro-antd/message';
import { NzModalModule } from 'ng-zorro-antd/modal';
import { NzNotificationModule } from 'ng-zorro-antd/notification';
import { NzPopconfirmModule } from 'ng-zorro-antd/popconfirm';
import { NzPopoverModule } from 'ng-zorro-antd/popover';
import { NzProgressModule } from 'ng-zorro-antd/progress';
import { NzRadioModule } from 'ng-zorro-antd/radio';
import { NzResultModule } from 'ng-zorro-antd/result';
import { NzSelectModule } from 'ng-zorro-antd/select';
import { NzSkeletonModule } from 'ng-zorro-antd/skeleton';
import { NzSpinModule } from 'ng-zorro-antd/spin';
import { NzStatisticModule } from 'ng-zorro-antd/statistic';
import { NzStepsModule } from 'ng-zorro-antd/steps';
import { NzSwitchModule } from 'ng-zorro-antd/switch';
import { NzTableModule } from 'ng-zorro-antd/table';
import { NzTabsModule } from 'ng-zorro-antd/tabs';
import { NzTagModule } from 'ng-zorro-antd/tag';
import { NzTimelineModule } from 'ng-zorro-antd/timeline';
import { NzTypographyModule } from 'ng-zorro-antd/typography';
import { NzUploadModule } from 'ng-zorro-antd/upload';

export const NG_ZORRO_MODULES = [
  // Angular Forms
  FormsModule,
  ReactiveFormsModule,
  
  // NG-ZORRO Components
  NzButtonModule,
  NzCheckboxModule,
  NzAffixModule,
  NzResultModule,
  NzBreadCrumbModule,
  NzFormModule,
  NzI18nModule,
  NzIconModule,
  NzInputModule,
  NzInputNumberModule,
  NzLayoutModule,
  NzMenuModule,
  NzMessageModule,
  NzNotificationModule,
  NzImageModule,
  NzStepsModule,
  NzSelectModule,
  NzDatePickerModule,
  NzTypographyModule,
  NzDropDownModule,
  NzCarouselModule,
  NzDividerModule,
  NzTagModule,
  NzSwitchModule,
  NzPopoverModule,
  NzCardModule,
  NzAvatarModule,
  NzAlertModule,
  NzUploadModule,
  NzSpinModule,
  NzModalModule,
  NzRadioModule,
  NzTableModule,
  NzProgressModule,
  NzSkeletonModule,
  NzEmptyModule,
  NzPopconfirmModule,
  NzAnchorModule,
  NzStatisticModule,
  NzListModule,
  NzTimelineModule,
  NzCollapseModule,
  NzTabsModule,
  NzBadgeModule
] as const;