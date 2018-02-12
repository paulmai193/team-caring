import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { AppointmentTcComponent } from './appointment-tc.component';
import { AppointmentTcDetailComponent } from './appointment-tc-detail.component';
import { AppointmentTcPopupComponent } from './appointment-tc-dialog.component';
import { AppointmentTcDeletePopupComponent } from './appointment-tc-delete-dialog.component';

@Injectable()
export class AppointmentTcResolvePagingParams implements Resolve<any> {

    constructor(private paginationUtil: JhiPaginationUtil) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
        const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'id,asc';
        return {
            page: this.paginationUtil.parsePage(page),
            predicate: this.paginationUtil.parsePredicate(sort),
            ascending: this.paginationUtil.parseAscending(sort)
      };
    }
}

export const appointmentRoute: Routes = [
    {
        path: 'appointment-tc',
        component: AppointmentTcComponent,
        resolve: {
            'pagingParams': AppointmentTcResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Appointments'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'appointment-tc/:id',
        component: AppointmentTcDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Appointments'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const appointmentPopupRoute: Routes = [
    {
        path: 'appointment-tc-new',
        component: AppointmentTcPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Appointments'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'appointment-tc/:id/edit',
        component: AppointmentTcPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Appointments'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'appointment-tc/:id/delete',
        component: AppointmentTcDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Appointments'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
