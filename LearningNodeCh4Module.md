#Learning Node Ch4 Modules

# 4장 node.js 모듈 시스템 #


## 개념 ##
  1. 모듈이란 class, package(java), namespace(c++)과 비슷하다고 생각하면 83% 먹고 들어간다.
  1. 모듈은 하나의 파일 혹은 하나의 디렉토리
  1. require는 import(java), include(c++)과 비슷하다고 생각하면 83% 먹고 들어간다.
  1. CommonJS Module system을 따랐다고 합니다.

## 사용 예 ##
```
 var mymodule = require('./mymodule'); //상대경로
 var mymodule = require('/home/myname/myapp/mymodule.js'); //절대경로
 var http = require('http'); //코어모듈
```
  1. 코어 모듈은 node\_modules 폴더에서 가져온다.
  1. require 한 다음 `http.createServer(...)` 이런 식으로 사용한다.

## node.js는 ##
  1. v8 + 수 많은 module
  1. 웹서버? 프레임워크?
  1. 직접만든 간단한 웹 서버
```
var http = require("http");

http.createServer(function(request, response) {
  response.writeHead(200, {"Content-Type": "text/plain"});
  response.write("Hello World");
  response.end();
}).listen(8888);
```

## 모듈을 만들어 보자 ##
  1. 외부 호출 항수는 export 명시
  1. 변수는 모듈 private
  1. 아까 그 웹 서버
```
var http = require("http");

function start() {
  function onRequest(request, response) {
    response.writeHead(200, {"Content-Type": "text/plain"});
    response.write("Hello World");
    response.end();
  }

  http.createServer(onRequest).listen(8888);
}

exports.start = start;

/*
// 실행하려면
var server = require("./server");
server.start();
//*/ 
```

## 디렉토리 패키징 ##
  1. 디렉토리 하나가 하나의 모듈
  1. 패키지 데스크립터(package.json) 제공 또는 진입점 파일(index.js)을 디렉토리에 포함

## node package manager ##
  1. 모듈 다운로드, 설치 등: 리눅스, 파이썬 등에서 사용하는 시스템
  1. 세상의 모든 module을 모은 저장소가 있다.
  1. 새 모듈을 설치 할 때는 저장소에서 다운 받는다.
  1. https://www.npmjs.org/
    1. 모듈 참 많다.

### npm 사용 ###
  1. 도움말: `npm help npm`
  1. 설치: `npm install modulename`
  1. 의존관계 모듈까지 알아서 설치해주니 편리
  1. 기타 여러 명령은 생략
    1. 특정 버전 설치, 제거, 업데이트, 설치된 목록

## 유명 모듈 ##
  1. http: node.js의 존재 이유라고나 할까
  1. socket.io: 소켓서버 만들 때
  1. express: 나름 프레임워크, 요즘 누가 http 모듈을 생으로 쓰니?
  1. request: HTTP request client
  1. async: 안 그대로 비동긴데 더 비동기로
  1. jade: jade 템플릿
  1. underscore: js 유틸리티 함수 모음
  1. connect: 미들웨어, 나름 유명한 듯
  1. uglify-js: 압축, 예쁘게 코드 정리
  1. 그 외
    1. Coffee-script, Optimist, Colors, Redis, Mime, JSDOM

## 쓸만한데 한 챕터로 다루기엔 작은 모듈 ##

### underscore ###
  1. 유틸리티
  1. 관습적으로 `var _ = require('underscore');`로 사용해서 underscore
    1. 또는 `var us = require('underscore');`
  1. 유틸리티
    1. each: `_.each(['apple','cherry'], function (fruit) { console.log(fruit); });`
    1. mixin: 기존 모듈에 함수 추가

### colors ###
  1. console.log()에 색과 스타일 추가
```
console.log('This Node kicks it!'.rainbow.underline);
```

### optimist ###
  1. 명령행 옵션 분석
```
//node ./app.js -o 1 -t 2

var argv = require('optimist').argv;
console.log(argv.o + " " + argv.t);
```

### jsdom ###
  1. html dom object
```
<!DOCTYPE html>
<html>
<head>
  <style>
  p { color:blue; margin:8px; }
  </style>
</head>
<body>
  <p id='test'>Test Paragraph.</p>
</body>
</html>
```
```
jsdom.env('./test.html',['http://code.jquery.com/jquery-1.7.1.min.js'], function(errors, window) {
  wind = window;
}); 
```
```
console.log(wind.$("#test").text());
```

## 조금은 어수선한 기타 정보 ##
  1. 모듈 확장자는 js, node, json
  1. 모듈을 찾지 못 했다면 자동으로 여러 디렉토리를 뒤져본다.
    1. 자세한 내용은 생략
  1. 모듈 unload
```
delete require.cache['./circle.js');
```

## 내가 만든 모듈 배포 ##
  1. 패키지 디스크립터(package descriptor)의 여러 필수 항목을 채운다.
    1. 이름, 설명, 버전 등
  1. `npm init`
  1. 하위 디렉토리: examples, test, doc
  1. `npm install. -g`, `npm adduser`, `npm publish`

(끝)