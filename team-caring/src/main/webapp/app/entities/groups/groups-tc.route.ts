import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { GroupsTcComponent } from './groups-tc.component';
import { GroupsTcDetailComponent } from './groups-tc-detail.component';
import { GroupsTcPopupComponent } from './groups-tc-dialog.component';
import { GroupsTcDeletePopupComponent } from './groups-tc-delete-dialog.component';

@Injectable()
export class GroupsTcResolvePagingParams implements Resolve<any> {

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

export const groupsRoute: Routes = [
    {
        path: 'groups-tc',
        component: GroupsTcComponent,
        resolve: {
            'pagingParams': GroupsTcResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Groups'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'groups-tc/:id',
        component: GroupsTcDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Groups'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const groupsPopupRoute: Routes = [
    {
        path: 'groups-tc-new',
        component: GroupsTcPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Groups'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'groups-tc/:id/edit',
        component: GroupsTcPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Groups'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'groups-tc/:id/delete',
        component: GroupsTcDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Groups'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
