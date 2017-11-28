import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { TeamTcComponent } from './team-tc.component';
import { TeamTcDetailComponent } from './team-tc-detail.component';
import { TeamTcPopupComponent } from './team-tc-dialog.component';
import { TeamTcDeletePopupComponent } from './team-tc-delete-dialog.component';

@Injectable()
export class TeamTcResolvePagingParams implements Resolve<any> {

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

export const teamRoute: Routes = [
    {
        path: 'team-tc',
        component: TeamTcComponent,
        resolve: {
            'pagingParams': TeamTcResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Teams'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'team-tc/:id',
        component: TeamTcDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Teams'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const teamPopupRoute: Routes = [
    {
        path: 'team-tc-new',
        component: TeamTcPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Teams'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'team-tc/:id/edit',
        component: TeamTcPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Teams'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'team-tc/:id/delete',
        component: TeamTcDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Teams'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
