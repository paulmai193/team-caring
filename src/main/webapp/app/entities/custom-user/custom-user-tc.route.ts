import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { CustomUserTcComponent } from './custom-user-tc.component';
import { CustomUserTcDetailComponent } from './custom-user-tc-detail.component';
import { CustomUserTcPopupComponent } from './custom-user-tc-dialog.component';
import { CustomUserTcDeletePopupComponent } from './custom-user-tc-delete-dialog.component';

@Injectable()
export class CustomUserTcResolvePagingParams implements Resolve<any> {

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

export const customUserRoute: Routes = [
    {
        path: 'custom-user-tc',
        component: CustomUserTcComponent,
        resolve: {
            'pagingParams': CustomUserTcResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'CustomUsers'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'custom-user-tc/:id',
        component: CustomUserTcDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'CustomUsers'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const customUserPopupRoute: Routes = [
    {
        path: 'custom-user-tc-new',
        component: CustomUserTcPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'CustomUsers'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'custom-user-tc/:id/edit',
        component: CustomUserTcPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'CustomUsers'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'custom-user-tc/:id/delete',
        component: CustomUserTcDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'CustomUsers'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
