var capacitorWifi = (function (exports, core) {
    'use strict';

    const Wifi = core.registerPlugin('Wifi', {
        web: () => Promise.resolve().then(function () { return web; }).then(m => new m.WifiWeb()),
    });

    class WifiWeb extends core.WebPlugin {
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

    var web = /*#__PURE__*/Object.freeze({
        __proto__: null,
        WifiWeb: WifiWeb
    });

    exports.Wifi = Wifi;

    Object.defineProperty(exports, '__esModule', { value: true });

    return exports;

})({}, capacitorExports);
//# sourceMappingURL=plugin.js.map
