import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { SubjectTcComponent } from './subject-tc.component';
import { SubjectTcDetailComponent } from './subject-tc-detail.component';
import { SubjectTcPopupComponent } from './subject-tc-dialog.component';
import { SubjectTcDeletePopupComponent } from './subject-tc-delete-dialog.component';

@Injectable()
export class SubjectTcResolvePagingParams implements Resolve<any> {

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

export const subjectRoute: Routes = [
    {
        path: 'subject-tc',
        component: SubjectTcComponent,
        resolve: {
            'pagingParams': SubjectTcResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Subjects'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'subject-tc/:id',
        component: SubjectTcDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Subjects'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const subjectPopupRoute: Routes = [
    {
        path: 'subject-tc-new',
        component: SubjectTcPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Subjects'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'subject-tc/:id/edit',
        component: SubjectTcPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Subjects'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'subject-tc/:id/delete',
        component: SubjectTcDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Subjects'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
