import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { NoteTcComponent } from './note-tc.component';
import { NoteTcDetailComponent } from './note-tc-detail.component';
import { NoteTcPopupComponent } from './note-tc-dialog.component';
import { NoteTcDeletePopupComponent } from './note-tc-delete-dialog.component';

@Injectable()
export class NoteTcResolvePagingParams implements Resolve<any> {

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

export const noteRoute: Routes = [
    {
        path: 'note-tc',
        component: NoteTcComponent,
        resolve: {
            'pagingParams': NoteTcResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Notes'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'note-tc/:id',
        component: NoteTcDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Notes'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const notePopupRoute: Routes = [
    {
        path: 'note-tc-new',
        component: NoteTcPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Notes'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'note-tc/:id/edit',
        component: NoteTcPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Notes'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'note-tc/:id/delete',
        component: NoteTcDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Notes'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
