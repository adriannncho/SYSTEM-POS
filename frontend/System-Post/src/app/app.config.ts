import { ApplicationConfig, provideZoneChangeDetection, importProvidersFrom } from '@angular/core';
import { provideRouter } from '@angular/router';

import { routes } from './app.routes';
import { es_ES, provideNzI18n } from 'ng-zorro-antd/i18n';
import { registerLocaleData } from '@angular/common';
import es from '@angular/common/locales/es';
import { FormsModule } from '@angular/forms';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { ICONS_MODULES } from './shared/config/icons.config';
import { authInterceptor } from './core/interceptors/auth/auth.interceptor';

registerLocaleData(es);

export const appConfig: ApplicationConfig = {
  providers: [
    provideZoneChangeDetection({ eventCoalescing: true }), 
    provideRouter(routes), 
    provideNzI18n(es_ES), 
    importProvidersFrom(FormsModule), 
    provideAnimationsAsync(), 
    provideHttpClient(
      withInterceptors([authInterceptor]) 
    ),
    importProvidersFrom(...ICONS_MODULES),
  ]
};
