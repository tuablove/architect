//
//  indexer.h
//  SearchDemo
//
//  Created by allting on 13. 6. 28..
//  Copyright (c) 2013년 allting. All rights reserved.
//

#ifndef __SearchDemo__indexer__
#define __SearchDemo__indexer__

#include <iostream>
#include <string>
#include <unordered_map>
#include <vector>
#include <set>

using namespace std;

typedef string document;
typedef set<document> postings;

typedef unordered_map<string, postings> indexer_map;


#endif /* defined(__SearchDemo__indexer__) */
