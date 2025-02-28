import { NzIconModule } from 'ng-zorro-antd/icon';
import { NZ_ICONS } from 'ng-zorro-antd/icon';
import { IconDefinition } from '@ant-design/icons-angular';

// Iconos
import { AuditOutline, CalendarOutline, CheckCircleOutline, ClearOutline, CloseCircleOutline, CustomerServiceOutline, DashboardOutline, DeleteOutline, DollarOutline, EyeOutline, FieldNumberOutline, FilePdfOutline, FileSearchOutline, FileTextOutline, FormOutline, GlobalOutline, GroupOutline, InfoCircleOutline, LeftCircleOutline, LoadingOutline, LockOutline, MailOutline, MenuFoldOutline, ProfileOutline, ReadOutline, ReloadOutline, RetweetOutline, SearchOutline, SecurityScanOutline, SwapOutline, SyncOutline, TableOutline, UserOutline, WarningOutline, YoutubeOutline } from '@ant-design/icons-angular/icons';
import { MenuUnfoldOutline } from '@ant-design/icons-angular/icons';

export const ANT_ICONS: IconDefinition[] = [
  MenuFoldOutline,
  MenuUnfoldOutline,
  FormOutline,
  DashboardOutline,
  SecurityScanOutline,
  SyncOutline,
  ReadOutline,
  UserOutline,
  FileSearchOutline,
  TableOutline,
  GlobalOutline,
  CustomerServiceOutline,
  MailOutline,
  FilePdfOutline,
  DeleteOutline,
  SwapOutline,
  WarningOutline,
  YoutubeOutline,
  FieldNumberOutline,
  RetweetOutline,
  AuditOutline,
  DollarOutline,
  FileTextOutline,
  ClearOutline,
  LeftCircleOutline,
  ProfileOutline,
  GroupOutline,
  SearchOutline,
  ReloadOutline,
  CloseCircleOutline,
  CheckCircleOutline,
  InfoCircleOutline,
  LoadingOutline,
  CalendarOutline,
  EyeOutline,
  LockOutline
];

export const ICONS_CONFIG = {
  provide: NZ_ICONS,
  useValue: ANT_ICONS
};

export const ICONS_MODULES = [NzIconModule] as const;