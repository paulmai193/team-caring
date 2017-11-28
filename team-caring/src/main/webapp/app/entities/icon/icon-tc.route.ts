import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { IconTcComponent } from './icon-tc.component';
import { IconTcDetailComponent } from './icon-tc-detail.component';
import { IconTcPopupComponent } from './icon-tc-dialog.component';
import { IconTcDeletePopupComponent } from './icon-tc-delete-dialog.component';

@Injectable()
export class IconTcResolvePagingParams implements Resolve<any> {

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

export const iconRoute: Routes = [
    {
        path: 'icon-tc',
        component: IconTcComponent,
        resolve: {
            'pagingParams': IconTcResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Icons'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'icon-tc/:id',
        component: IconTcDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Icons'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const iconPopupRoute: Routes = [
    {
        path: 'icon-tc-new',
        component: IconTcPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Icons'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'icon-tc/:id/edit',
        component: IconTcPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Icons'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'icon-tc/:id/delete',
        component: IconTcDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Icons'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
