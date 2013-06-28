//
//  main.cpp
//  Indexer
//
//  Created by allting on 13. 6. 28..
//  Copyright (c) 2013년 allting. All rights reserved.
//

#include <iostream>
//#include <boost/filesystem.hpp>

//using namespace boost::filesystem;
using namespace std;

int main(int argc, const char * argv[])
{
/*	path p (argv[1]);   // p reads clearer than argv[1] in the following code
	
	try
	{
		if (exists(p))    // does p actually exist?
		{
			if (is_regular_file(p))        // is p a regular file?
				cout << p << " size is " << file_size(p) << '\n';
			
			else if (is_directory(p))      // is p a directory?
			{
				cout << p << " is a directory containing:\n";
				
				copy(directory_iterator(p), directory_iterator(), // directory_iterator::value_type
					 ostream_iterator<directory_entry>(cout, "\n")); // is directory_entry, which is
				// converted to a path by the
				// path stream inserter
			}
			
			else
				cout << p << " exists, but is neither a regular file nor a directory\n";
		}
		else
			cout << p << " does not exist\n";
	}
	
	catch (const filesystem_error& ex)
	{
		cout << ex.what() << '\n';
	}
*/
	return 0;
}

