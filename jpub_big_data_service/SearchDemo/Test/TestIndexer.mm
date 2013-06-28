//
//  Test.m
//  Test
//
//  Created by allting on 13. 6. 28..
//  Copyright (c) 2013년 allting. All rights reserved.
//

#import "TestIndexer.h"

#include "Indexer.h"
#include <algorithm>
#include <sstream>

#include <boost/algorithm/string.hpp>

@implementation TestIndexer

- (void)setUp
{
    [super setUp];
    
    // Set-up code here.
}

- (void)tearDown
{
    // Tear-down code here.
    
    [super tearDown];
}

- (void)testIndexer
{
	struct tag_test
	{
		string keyword;
		postings posts;
	}test[]={
		{"key1", postings{"test1.txt", "test2.txt"}},
		{"key3", postings{"test1.txt", "test3.txt"}},
		{"key5", postings{"test3.txt", "test5.txt", "test7.txt"}}
	};
	
	const size_t test_size = sizeof(test)/sizeof(test[0]);
	
	indexer_map map;
	
	for(size_t i=0; i<test_size; i++)
	{
		map.insert(make_pair(test[i].keyword, test[i].posts));
	}
	
	STAssertTrue(test_size == map.size(), @"Must be true");
	
	for(size_t i=0; i<test_size; i++)
	{
		postings results = map[test[i].keyword];
		STAssertTrue(test[i].posts.size() == results.size(), @"Must be equal");
		STAssertTrue(std::equal(results.begin(), results.end(), test[i].posts.begin()), @"Must be equal");
	}
}

-(void)testCreateIndexer
{
	struct tag_test
	{
		string name;
		string contents;
	}test[] = {
		{"test1.txt", "it is what it is"},
		{"test2.txt", "what is it"},
		{"test3.txt", "it is a banana"}
	};
	
	struct tag_expect
	{
		string term;
		postings posts;
	}expect[] = {
		{"a", postings{"test3.txt"}},
		{"banana", postings{"test3.txt"}},
		{"is", postings{"test1.txt", "test2.txt", "test3.txt"}},
		{"it", postings{"test1.txt", "test2.txt", "test3.txt"}},
		{"what", postings{"test1.txt", "test2.txt"}}
	};
	
	const size_t test_size = sizeof(test)/sizeof(test[0]);
	const size_t expect_size = sizeof(expect)/sizeof(expect[0]);
	
	indexer_map map;
	for(size_t i=0; i<test_size; i++)
	{
		vector<string> terms;
		boost::split(terms, test[i].contents, boost::is_any_of(" "));
		
		for_each(terms.begin(), terms.end(), [&](string term){
			if(map.find(term)==map.end()){
				map.insert(make_pair(term, postings{test[i].name}));
			}else{
				postings &posts = map.at(term);
				posts.insert(test[i].name);
			}
		});
	}

	for(size_t i=0; i<expect_size; i++)
	{
		postings results = map.at(expect[i].term);
		
		STAssertTrue(expect[i].posts.size() == results.size(), @"Must be equal");
		STAssertTrue(std::equal(results.begin(), results.end(), expect[i].posts.begin()), @"Must be equal");
	}
}

-(void)testStringSplitBySpace{
    string sentence = "And I feel fine...";
	vector<string> expect = {"And", "I", "feel", "fine..."};
	vector<string> results;
	
    istringstream iss(sentence);
    copy(istream_iterator<string>(iss),
		 istream_iterator<string>(),
		 back_inserter<vector<string> >(results));
	
	STAssertTrue(expect.size() == results.size(), @"Must be equal");
	STAssertTrue(std::equal(results.begin(), results.end(), expect.begin()), @"Must be equal");
}

-(void)testStringSplitByDelimitterUsingBoost{
    string sentence = "And I feel fine...";
	vector<string> expect = {"And", "I", "feel", "fine..."};
	vector<string> results;
	
	boost::split(results, sentence, boost::is_any_of("\t "));
	
	STAssertEquals(expect.size(), results.size(), @"Must be equal");
	STAssertTrue(std::equal(results.begin(), results.end(), expect.begin()), @"Must be equal");
}

@end
