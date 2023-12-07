import { environment } from "src/environments/environment";

const PORT = '50004';
const SERVICE = 'stcu2service';

export const API = '/api';

export const HTTPCONFIG = {
    /* url: environment.production ? 
    window.location.host+'/stcu2service': 
    'http://localhost:50000/stcu2service' */

    url: 'http://' + window.location.hostname + ':' + PORT + '/' + SERVICE
}