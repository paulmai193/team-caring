import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { NotificationTcComponent } from './notification-tc.component';
import { NotificationTcDetailComponent } from './notification-tc-detail.component';
import { NotificationTcPopupComponent } from './notification-tc-dialog.component';
import { NotificationTcDeletePopupComponent } from './notification-tc-delete-dialog.component';

@Injectable()
export class NotificationTcResolvePagingParams implements Resolve<any> {

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

export const notificationRoute: Routes = [
    {
        path: 'notification-tc',
        component: NotificationTcComponent,
        resolve: {
            'pagingParams': NotificationTcResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Notifications'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'notification-tc/:id',
        component: NotificationTcDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Notifications'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const notificationPopupRoute: Routes = [
    {
        path: 'notification-tc-new',
        component: NotificationTcPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Notifications'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'notification-tc/:id/edit',
        component: NotificationTcPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Notifications'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'notification-tc/:id/delete',
        component: NotificationTcDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Notifications'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
