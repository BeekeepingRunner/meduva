import {Injectable} from "@angular/core";
import {
  HTTP_INTERCEPTORS,
  HttpErrorResponse,
  HttpEvent,
  HttpHandler,
  HttpInterceptor,
  HttpRequest
} from "@angular/common/http";
import {JwtStorageService} from "../service/token/jwt-storage.service";
import {BehaviorSubject, Observable, throwError} from "rxjs";
import {catchError, filter, switchMap, take} from "rxjs/operators";
import {AuthService} from "../service/auth/auth.service";

const TOKEN_HEADER_KEY = 'Authorization';
const TOKEN_PREFIX = 'Bearer ';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

  private isRefreshing = false;
  private refreshTokenSubject: BehaviorSubject<any> = new BehaviorSubject<any>(null);

  constructor(
    private jwtStorageService: JwtStorageService,
    private authService: AuthService)
  {
  }

  // Adds Authorization header to the request, with 'Bearer' prefix to the token
  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<any> {
    let authReq = req;
    const tokenStr = this.jwtStorageService.getToken();
    if (tokenStr != null) {
      authReq = this.addTokenHeader(req, tokenStr);
    }

    return next.handle(authReq).pipe(catchError( err => {
      if (err instanceof HttpErrorResponse
        && !authReq.url.includes('auth/signin')
        && err.status == 401) {
        return this.handle401Error(authReq, next);
      }
      return throwError(err);
    }));
  }

  private addTokenHeader(req: HttpRequest<any>, token: string) {
    return req.clone({
      headers: req.headers.set(TOKEN_HEADER_KEY, TOKEN_PREFIX + token)
    });
  }

  private handle401Error(request: HttpRequest<any>, next: HttpHandler) {
    if (!this.isRefreshing) {
      this.isRefreshing = true;
      this.refreshTokenSubject.next(null);

      const token = this.jwtStorageService.getRefreshToken();
      if (token) {
        return this.authService.refreshToken(token).pipe(
          switchMap((token: any) => {
            this.isRefreshing = false;
            this.jwtStorageService.saveToken(token.accessToken);
            this.refreshTokenSubject.next(token.accessToken);
            return next.handle(this.addTokenHeader(request, token.accessToken));
          }),
          catchError((err) => {
            this.isRefreshing = false;
            this.jwtStorageService.signOut();
            return throwError(err);
          })
        );
      }

      return this.refreshTokenSubject.pipe(
        filter(token => token !== null),
        take(1),
        switchMap((token) =>
          next.handle(this.addTokenHeader(request, token)))
      );
    }
  }

}

export const authInterceptorProviders = [
  {
    provide: HTTP_INTERCEPTORS,
    useClass: AuthInterceptor,
    multi: true
  }
]
