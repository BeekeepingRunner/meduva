import {Injectable} from "@angular/core";
import {HTTP_INTERCEPTORS, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from "@angular/common/http";
import {JwtStorageService} from "../service/token/jwt-storage.service";
import {Observable} from "rxjs";

const TOKEN_HEADER_KEY = 'Authorization';
const TOKEN_PREFIX = 'Bearer ';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

  constructor(private tokenService: JwtStorageService)
  {
  }

  // Adds Authorization header to the request, with 'Bearer' prefix to the token
  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {

    let authReq = req;
    const tokenStr = this.tokenService.getToken();

    if (tokenStr != null) {
      authReq = req.clone({
        headers: req.headers.set(TOKEN_HEADER_KEY, TOKEN_PREFIX + tokenStr)
      });
    }

    return next.handle(authReq);
  }
}

export const authInterceptorProviders = [
  {
    provide: HTTP_INTERCEPTORS,
    useClass: AuthInterceptor,
    multi: true
  }
]
