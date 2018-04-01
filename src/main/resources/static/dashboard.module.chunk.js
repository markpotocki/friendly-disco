webpackJsonp(["dashboard.module"],{

/***/ "./src/app/dashboard/dashboard.component.css":
/***/ (function(module, exports) {

module.exports = ""

/***/ }),

/***/ "./src/app/dashboard/dashboard.component.html":
/***/ (function(module, exports) {

module.exports = "\n<div class=\"container\">\n\n  <!-- storage -->\n<section class=\"py-2\">\n  <h2>My Storage</h2>\n  <div class=\"card-group\">\n\n    <div class=\"card\">\n        <div class=\"card-img-top text-center fa-5x\">\n          <span class=\"fa-layers fa-fw\">\n            <i class=\"far fa-hdd\"></i>\n            <i class=\"fas fa-exclamation-triangle text-warning\" data-fa-transform=\"shrink-8 right-8 down-5\"></i>\n          </span>\n          </div>\n\n      <div class=\"card-body\">\n        <h5 class=\"card-title\">Storage</h5>\n        <p class=\"card-text\">View or manage your currently uploaded files</p>\n\n        <a class=\"btn primary-gradient btn-primary-gradient float-right\">Open</a>\n      </div>\n    </div>\n\n\n    <!-- upload card -->\n    <div class=\"card\">\n        <div class=\"card-img-top text-center fa-5x\">\n          <span class=\"fa-layers fa-fw\">\n            <i class=\"fas fa-upload\"></i>\n          </span>\n        </div>\n\n      <div class=\"card-body\">\n        <h5 class=\"card-title\">Upload Files</h5>\n        <p class=\"card-text\">Upload a file or archive to your online storage drive</p>\n\n        <a class=\"btn primary-gradient btn-primary-gradient float-right\" routerLink=\"/storage/upload\">Upload</a>\n      </div>\n    </div>\n\n\n    <!-- lock drive -->\n    <div class=\"card\">\n      <div class=\"card-img-top text-center fa-5x\">\n        <i class=\"fas fa-unlock\"></i>\n      </div>\n\n      <div class=\"card-body\">\n        <h5 class=\"card-title\">Lock Drive</h5>\n        <p class=\"card-text\">Lock your drive down to shutdown access to the drive</p>\n\n        <a class=\"btn danger-gradient btn-danger-gradient float-right\">Lock Drive</a>\n      </div>\n    </div>\n\n  </div>\n</section>\n\n<!-- home -->\n<section class=\"py-2\">\n  <h2>My Home</h2>\n  <div class=\"card-group\">\n\n    <!-- lights control -->\n    <div class=\"card\">\n      <div class=\"card-img-top text-center fa-5x\">\n          <span class=\"fa-layers fa-fw\">\n              <i class=\"fas fa-lightbulb\"></i>\n              <i class=\"fas fa-leaf text-success\" data-fa-transform=\"shrink-10 down-7 right-3\"></i>\n            </span>\n      </div>\n\n      <div class=\"card-body\">\n        <h5 class=\"card-title\">Lights</h5>\n        <p class=\"card-text\">Turn lights on or off or set a scheduler for turing on lights</p>\n\n        <a class=\"btn primary-gradient btn-primary-gradient float-right\" routerLink=\"/home/lights\">Manage Lights</a>\n      </div>\n    </div>\n\n    <!-- thermostat control -->\n    <div class=\"card\">\n        <div class=\"card-img-top text-center fa-5x\">\n          <span class=\"fa-layers fa-fw\">\n            <i class=\"fas fa-thermometer-half\"></i>\n            <i class=\"fas fa-fire text-danger\" data-fa-transform=\"shrink-10 down-6 right-3\"></i>\n          </span>\n        </div>\n  \n        <div class=\"card-body\">\n          <h5 class=\"card-title\">Tempature</h5>\n          <p class=\"card-text\">Change the tempature or set programming for thermostat</p>\n  \n          <a class=\"btn primary-gradient btn-primary-gradient float-right\" routerLink=\"/home/thermostat\">Manage Tempature</a>\n        </div>\n      </div>\n\n      <!-- entertainment control -->\n      <div class=\"card\">\n          <div class=\"card-img-top text-center fa-5x\">\n            <span class=\"fa-layers fa-fw\">\n              <i class=\"fas fa-play\"></i>\n              <i class=\"fas fa-volume-up text-warning\" data-fa-transform=\"shrink-10 down-7 right-5\"></i>\n            </span>\n          </div>\n    \n          <div class=\"card-body\">\n            <h5 class=\"card-title\">MediaCast</h5>\n            <p class=\"card-text\">Play video, music, or photos on connected devices</p>\n    \n            <a class=\"btn primary-gradient btn-primary-gradient float-right\">Open MediaCast</a>\n          </div>\n        </div>\n\n  </div>\n</section>\n\n</div>\n"

/***/ }),

/***/ "./src/app/dashboard/dashboard.component.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return DashboardComponent; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__("./node_modules/@angular/core/esm5/core.js");
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};

var DashboardComponent = /** @class */ (function () {
    function DashboardComponent() {
    }
    DashboardComponent.prototype.ngOnInit = function () {
    };
    DashboardComponent = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["n" /* Component */])({
            selector: 'app-dashboard',
            template: __webpack_require__("./src/app/dashboard/dashboard.component.html"),
            styles: [__webpack_require__("./src/app/dashboard/dashboard.component.css")]
        }),
        __metadata("design:paramtypes", [])
    ], DashboardComponent);
    return DashboardComponent;
}());



/***/ }),

/***/ "./src/app/dashboard/dashboard.module.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
Object.defineProperty(__webpack_exports__, "__esModule", { value: true });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "DashboardModule", function() { return DashboardModule; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__("./node_modules/@angular/core/esm5/core.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__angular_common__ = __webpack_require__("./node_modules/@angular/common/esm5/common.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__dashboard_component__ = __webpack_require__("./src/app/dashboard/dashboard.component.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__angular_router__ = __webpack_require__("./node_modules/@angular/router/esm5/router.js");
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};




var routes = [
    { path: '', component: __WEBPACK_IMPORTED_MODULE_2__dashboard_component__["a" /* DashboardComponent */] }
];
var DashboardModule = /** @class */ (function () {
    function DashboardModule() {
    }
    DashboardModule = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["I" /* NgModule */])({
            imports: [
                __WEBPACK_IMPORTED_MODULE_1__angular_common__["b" /* CommonModule */],
                __WEBPACK_IMPORTED_MODULE_3__angular_router__["b" /* RouterModule */].forChild(routes)
            ],
            declarations: [__WEBPACK_IMPORTED_MODULE_2__dashboard_component__["a" /* DashboardComponent */]]
        })
    ], DashboardModule);
    return DashboardModule;
}());



/***/ })

});
//# sourceMappingURL=dashboard.module.chunk.js.map