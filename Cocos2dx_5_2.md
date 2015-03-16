#만들면서 배우는 Cocos2d-x 5.2~3



# 5.2. 게임 오브젝트 배치 #
## 5.2.1. 게임 오브젝트 그리기 ##
```
class GameLayer : public CCLayer
{
public:
	bool init();

	void StartGame();            // 게임 시작시 초기화 함수

	static CCScene* scene();

	CREATE_FUNC(GameLayer);

protected:

	CCSize m_winSize;        // 윈도우의 크기를 구하기 위해 함수를 매번 호출하는 것을 피함
                                            // CCDirector::sharedDirector()->getWinSize();
};
```

```
bool GameLayer::init()
{
	if (CCLayer::init() == false)
	{
		return false;
	}

	CCSprite* pBackgroundSprite = CCSprite::create("Background.png");
	pBackgroundSprite->setPosition(CCPointZero);
	pBackgroundSprite->setAnchorPoint(ccp(0.0f, 0.0f));
	addChild(pBackgroundSprite, zBackground);

	m_winSize = CCDirector::sharedDirector()->getWinSize();

	StartGame();

	return true;
}

// setAnchorPoint 사용 안함.
void GameLayer::StartGame()
{
	CCSprite* pGameObject = CCSprite::create("Blue.png");
	pGameObject->setPosition(ccp(m_winSize.width / 2, m_winSize.height/2));
	addChild(pGameObject);
}
```

_setAnchorPoint_ 함수는 이미지 위치를 조정할때 기준점을 정함. 생략시 이미지의 정 가운데 좌표를 사용

## 5.2.2. 게임 오브젝트 배치하기 ##
공통 코드를 별도로 정리 Common.h
```
#include "cocos2d.h"

#define DESIGN_WIDTH	768.0f
#define DESIGN_HEIGHT	1024.0f

#define ROW_COUNT			8
#define COLUMN_COUNT		8
#define MAX_ROW_COUNT		10

#define TYPE_COUNT			7

#define OBJECT_WIDTH		96
#define OBJECT_HEIGHT		96
#endif
```

보드 좌표 저장을 위한 변수 선언
```
class GameLayer : public CCLayer
{
public:
...
protected:
	CCSprite* m_pBoard[COLUMN_COUNT][MAX_ROW_COUNT];  //보드 오브젝트 저장용
...
};
```

게임오브젝트 배치
```
void GameLayer::StartGame()
{
	for (int x = 0; x < COLUMN_COUNT; ++x)
	{
		for (int y = 0; y < ROW_COUNT; ++y)
		{
			CCSprite* pGameObject = CCSprite::create("Blue.png");
			m_pBoard[x][y] = pGameObject;
            
			float xPos = floorf(x * OBJECT_WITDH);
			// cocos2d-x의 경우 왼쪽 아래부터 0, 0 좌표 시작
			float yPos = m_winSize.height - floorf(y * OBJECT_HEIGHT);


			pGameObject->setAnchorPoint(ccp(0.0f, 1.0f));
			pGameObject->setPosition(ccp(xPos, yPos));

			addChild(pGameObject, 1);
		}
	}
}
```

## 5.2.3. 7가지 종류의 게임 오브젝트 배치하기 ##
랜덤하게 7가지 종류의 오브젝트를 배치
```
void GameLayer::StartGame()
{
	srand(time(NULL));

	static std::string objectNames[TYPE_COUNT] =
	{
			"Blue.png",
			"Brown.png",
			"Green.png",
			"Pink.png",
			"Purple.png",
			"Red.png",
			"Yellow.png",
	};

	for (int x = 0; x < COLUMN_COUNT; ++x)
	{
		for (int y = 0; y < ROW_COUNT; ++y)
		{
			int type = rand() % TYPE_COUNT;

			CCSprite* pGameObject = CCSprite::create(objectNames[type].c_str());
			m_pBoard[x][y] = pGameObject;

			float xPos = floorf(x * OBJECT_WITDH);
			// cocos2d-x의 경우 왼쪽 아래부터 0, 0 좌표 시작
			float yPos = m_winSize.height - floorf(y * OBJECT_HEIGHT);

			pGameObject->setAnchorPoint(ccp(0.0f, 1.0f));
			pGameObject->setPosition(ccp(xPos, yPos));

			addChild(pGameObject, 1);
		}
	}
}

```
## 5.2.4. 좌표 계산 유틸리티 클래스 ##
좌표 변환 클래스를 공통 부분으로 분리
```
float xPos = floorf(x * OBJECT_WITDH);
float yPos = m_winSize.height - floorf(y * OBJECT_HEIGHT);
```

```
class Common
{
public:
	static float ComputeX(float x);
	static float ComputeY(float y);
	static CCPoint ComputeXY(float x, float y);

	static int ComputeBoardX(float x);
	static int ComputeBoardY(float y);
};
```

```
#include "Common.h"

float Common::ComputeX(float x)
{
	return floorf(x * OBJECT_WIDTH);
}


float Common::ComputeY(float y)
{
	return CCDirector::sharedDirector()->getWinSize().height - floorf(y * OBJECT_HEIGHT);
}

CCPoint Common::ComputeXY(float x, float y)
{
	return ccp(ComputeX(x), ComputeY(y));
}

int Common::ComputeBoardX(float x)
{
	return (int)(x / floorf(OBJECT_WIDTH));
}

int Common::ComputeBoardY(float y)
{
	return (int)(y / floorf(OBJECT_HEIGHT));
}
```

분리한 이후의 코드
```
void GameLayer::StartGame()
{
	....

	for (int x = 0; x < COLUMN_COUNT; ++x)
	{
		for (int y = 0; y < ROW_COUNT; ++y)
		{
			int type = rand() % TYPE_COUNT;

			CCSprite* pGameObject = CCSprite::create(objectNames[type].c_str());
			m_pBoard[x][y] = pGameObject;

			pGameObject->setAnchorPoint(ccp(0.0f, 1.0f));
			pGameObject->setPosition(Common::ComputeXY(x, y));

			addChild(pGameObject, 1);
		}
	}
}

```

## 5.2.5. Z Order ##
_addChild_ 의 두번째 매개변수는 Z order을 의미, 여러 오브젝트들의 그리는 순서를 결정.
기본 값은 0이고, 0이 제일 먼저 그려짐.
```
    /** Adds a child to the container with z-order as 0.
     If the child is added to a 'running' node, then 'onEnter' and 'onEnterTransitionDidFinish' will be called immediately.
     @since v0.7.1
     */
    virtual void addChild(CCNode * child);

    /** Adds a child to the container with a z-order
     If the child is added to a 'running' node, then 'onEnter' and 'onEnterTransitionDidFinish' will be called immediately.
     @since v0.7.1
     */
    virtual void addChild(CCNode * child, int zOrder);

    /** Adds a child to the container with z order and tag
     If the child is added to a 'running' node, then 'onEnter' and 'onEnterTransitionDidFinish' will be called immediately.
     @since v0.7.1
     */
    virtual void addChild(CCNode * child, int zOrder, int tag);
```

Z order의 반영
```
enum
{
	zBackground = 0,
	zGameObject = 1,
};

bool GameLayer::init()
{
	if (CCLayer::init() == false)
	{
		return false;
	}

	CCSprite* pBackgroundSprite = CCSprite::create("Background.png");
	pBackgroundSprite->setPosition(CCPointZero);
	pBackgroundSprite->setAnchorPoint(ccp(0.0f, 0.0f));
	addChild(pBackgroundSprite, zBackground);

	m_winSize = CCDirector::sharedDirector()->getWinSize();

	StartGame();

	return true;
}

void GameLayer::StartGame()
{
	.....

	for (int x = 0; x < COLUMN_COUNT; ++x)
	{
		for (int y = 0; y < ROW_COUNT; ++y)
		{
			int type = rand() % TYPE_COUNT;

			CCSprite* pGameObject = CCSprite::create(objectNames[type].c_str());
			m_pBoard[x][y] = pGameObject;

			pGameObject->setAnchorPoint(ccp(0.0f, 1.0f));
			pGameObject->setPosition(Common::ComputeXY(x, y));

			addChild(pGameObject, zGameObject);
		}
	}
}


```

# 5.3. 게임 오브젝트 클래스 생성 #
게임 오브젝트의 기능이 많아질 경우 책임을 분리하는 것이 유리
```
class GameObject : public CCSprite
{
public:
	GameObject();
	~GameObject();

protected:
	static GameObject* create(const char* pszFileName, const CCRect& rect);

public:
	static GameObject* Create(int type);

	int GetType();
	void SetType(int type);

private:
	int m_type;
};
```

```


GameObject* GameObject::create(const char* pszFileName, const CCRect& rect)
{
	GameObject* pSprite = new GameObject();
	if (pSprite && pSprite->initWithFile(pszFileName, rect))
	{
		CCLog("pSprite ref = %d", pSprite->m_uReference);
		pSprite->autorelease();
		CCLog("after pSprite ref = %d", pSprite->m_uReference);
		return pSprite;
	}

	CC_SAFE_DELETE(pSprite);
	return NULL;
}

GameObject* GameObject::Create(int type)
{
	static std::string objectNames[TYPE_COUNT] =
	{
			"Blue.png",
			"Brown.png",
			"Green.png",
			"Pink.png",
			"Purple.png",
			"Red.png",
			"Yellow.png",
	};

	if (type < 0 || type > TYPE_COUNT - 1)
		return NULL;

	GameObject* pGameObject = GameObject::create(objectNames[type].c_str(), CCRectMake(0.0f, 0.0f, OBJECT_WIDTH, OBJECT_HEIGHT));
	pGameObject->SetType(type);

	return pGameObject;
}

int GameObject::GetType()
{
	return m_type;
}

void GameObject::SetType(int type)
{
	m_type = type;
}
```

결국 _StartGame_ 는 단순한 형태가 됨
```
void GameLayer::StartGame()
{
	srand(time(NULL));

	for (int x = 0; x < COLUMN_COUNT; ++x)
	{
		for (int y = 0; y < ROW_COUNT; ++y)
		{
			int type = rand() % TYPE_COUNT;

			GameObject* pGameObject = GameObject::Create(type);
			m_pBoard[x][y] = pGameObject;

			pGameObject->setAnchorPoint(ccp(0.0f, 1.0f));
			pGameObject->setPosition(Common::ComputeXY(x, y));

			addChild(pGameObject, zGameObject);
		}
	}
}

```
## 5.3.1. cocos2d-x  메모리 관리 ##
Cocos2d-x에서는 레퍼런스 카운트를 이용해서 메모리를 관리
```
{
	A a;	//레퍼런스 카운트 1
}
// 이부분에서 a객체는 레퍼런스 카운트가 0이 되면서 소멸
```

```
{
	A a;	//레퍼런스 카운트 1
	array.add(a);	// 레퍼런스 카운트 2
	array.remove(a);	// 레퍼런스 카운트 1
}
// 이부분에서 a객체는 레퍼런스 카운트가 0이 되면서 소멸
```

Cocos 2D의 최상위 Object
```
class CC_DLL CCObject : public CCCopying
{
public:
    // object id, CCScriptSupport need public m_uID
    unsigned int        m_uID;
    // Lua reference id
    int                 m_nLuaID;
protected:
    // count of references
    unsigned int        m_uReference;
    // count of autorelease, autorelease풀을 사용할때의 카운터
    unsigned int        m_uAutoReleaseCount;
    ...
};

```