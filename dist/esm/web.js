import { WebPlugin } from '@capacitor/core';
export class WifiWeb extends WebPlugin {
    constructor() {
        super();
        window.screen.orientation.addEventListener("change", () => {
            const type = window.screen.orientation.type;
            console.log("Changement de l'orientation de l'Ecran: ", type);
            this.notifyListeners("screenOrientationChange", { type });
        });
    }
    async getIP() {
        return { ip: null };
    }
    async getSSID() {
        return { ssid: null };
    }
    async connect(options) {
        console.log(options);
        return { ssid: null };
    }
    async connectPrefix(options) {
        console.log(options);
        return { ssid: null };
    }
    async disconnect() {
        return;
    }
}
//# sourceMappingURL=web.js.map