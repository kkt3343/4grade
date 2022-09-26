// ==UserScript==
// @name         New Userscript
// @namespace    http://tampermonkey.net/
// @version      0.1
// @description  try to take over the world!
// @author       You
// @match        https://loawa.com/
// @icon         https://www.google.com/s2/favicons?sz=64&domain=loawa.com
// @grant        none
// ==/UserScript==

(function() {
    'use strict';
    function A(){
        var c = document.getElementsByClassName('row m-0 p-0 justify-content-center'); var g = c.length;
        for(var i=0;i<g;i++){c[0].remove()};
    }
    setTimeout(A, 200);
    // Your code here...
})();