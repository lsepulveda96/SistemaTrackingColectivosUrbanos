import { environment } from "src/environments/environment";

export const HTTPCONFIG = {
   url: environment.production ? 'http://localhost:50004/stcu2service': 'http://localhost:50000/stcu2'
    //url: 'http://localhost:50000'
}

export const API = '/api';