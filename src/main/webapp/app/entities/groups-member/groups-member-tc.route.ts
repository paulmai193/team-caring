import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { GroupsMemberTcComponent } from './groups-member-tc.component';
import { GroupsMemberTcDetailComponent } from './groups-member-tc-detail.component';
import { GroupsMemberTcPopupComponent } from './groups-member-tc-dialog.component';
import { GroupsMemberTcDeletePopupComponent } from './groups-member-tc-delete-dialog.component';

@Injectable()
export class GroupsMemberTcResolvePagingParams implements Resolve<any> {

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

export const groupsMemberRoute: Routes = [
    {
        path: 'groups-member-tc',
        component: GroupsMemberTcComponent,
        resolve: {
            'pagingParams': GroupsMemberTcResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'GroupsMembers'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'groups-member-tc/:id',
        component: GroupsMemberTcDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'GroupsMembers'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const groupsMemberPopupRoute: Routes = [
    {
        path: 'groups-member-tc-new',
        component: GroupsMemberTcPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'GroupsMembers'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'groups-member-tc/:id/edit',
        component: GroupsMemberTcPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'GroupsMembers'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'groups-member-tc/:id/delete',
        component: GroupsMemberTcDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'GroupsMembers'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
