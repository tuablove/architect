//
//  TestSearch.m
//  SearchDemo
//
//  Created by allting on 13. 6. 29..
//  Copyright (c) 2013년 allting. All rights reserved.
//

#import "TestSearch.h"

#include "Indexer.h"
#include <algorithm>
#include <sstream>

#include <boost/algorithm/string.hpp>

@implementation TestSearch

-(void)testSearch{
	struct tag_test
	{
		string name;
		string contents;
	}test[] = {
		{"test1.txt", "it is what it is"},
		{"test2.txt", "what is it"},
		{"test3.txt", "it is a banana"}
	};
	
	const size_t test_size = sizeof(test)/sizeof(test[0]);
	
	indexer_map map;
	for(size_t i=0; i<test_size; i++)
	{
		vector<string> terms;
		boost::split(terms, test[i].contents, boost::is_any_of(" "));
		
		for_each(terms.begin(), terms.end(), [&map, test, i](string term){
			if(map.find(term)==map.end()){
				map.insert(make_pair(term, postings{test[i].name}));
			}else{
				postings &posts = map.at(term);
				posts.insert(test[i].name);
			}
		});
	}


	// search
	const vector<string> keywords = {"what", "it", "is", "banana", "notfound"};
	
	for_each(keywords.begin(), keywords.end(), [&](string keyword){
		if(map.find(keyword)==map.end()){
			cout << "Not Found - " << keyword << endl;
		}else{
			cout << "Found - " << keyword << endl;
			postings &posts = map.at(keyword);
			copy(posts.begin(), posts.end(), ostream_iterator<string>(cout, " "));
			cout<< endl;
		}
	});

}

@end
