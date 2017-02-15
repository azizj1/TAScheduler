#Teaching Assistant Scheduler
*Note:* This was a class project for my Intro to Software Engineering in Spring 2012. 

Our objective was to create a program that will take in appropriate information provided by both the instructor and the TAs and produce the best schedule possible. This schedule will never conflict with any of the TA’s class, will not conflict with a TA’s other requests off unless necessary, and will meet as many TA preferences as possible. By acquiring this program the instructors will have less to worry about concerning their courses at the beginning of the semester and will be able to devote more time to their regular teaching and research duties.

The instructor must provide the following:
* Labs
   * 
   * Lab start time
   * Lab end time
   * Lab day
* TAs
   * TA name
   * TA id
   * Number of Sections each TA must be assigned
   * Number of Sections each TA must specify as preferred

The TAs must provide
* TA name
* TA id
* Classes they're taking
   * Class start time
   * Class end time
   * Day(s) of the week
* Avoidance time preferred
   * Start time
   * End time
   * Day(s) of the week
   * Reason

The program must then be able to read in and interpret these files correctly. To begin, it must be able to catch any errors that occur in the files submitted by the actors outside the system. Errors such as invalid time periods, invalid names, and invalid preferences must be caught before the program even begins to create an ideal scheduler. These errors should be corrected and the author must be notified before a valid solution can be found and produced.

After the files have been validated and entered correctly this program must begin to sort through all the information provided. The first priority is for the TAs to not be scheduled during their normal class time hours. Classes are a TAs first priority and they should never be required to miss a class in order to teach a lab section. If all lab sections cannot be filled because all the TAs have class during a certain lab time, the instructor should immediately be notified a solution does not exist so we can look into hiring other TAs. Secondly, as many TAs as possible should not be required to teach during their requested time off (avoidance times). Some TAs have jobs and other commitments outside of their lives on the college campus and should be able to carry on with these aspects of their lives it possible. Lastly, as many TAs as possible should receive their preferred lab sections. Simply because a solution exists with TAs placed in labs does not mean it is the best solution. TAs are respected employees and their preferences should be honored. As a result, fulfilling their requests if possible is vital.

Fairness is also a salient factor. If one TA is assigned 2 labs in time slots he didn't want, and every other TA gets preferred sections, then the solution isn't very fair. Labs should be equally distributed. There is a simple solution to considering fairness: if one TA has his avoidance neglected, then all the other TAs should have at least their non-avoidance neglected, i.e., labs can be put in times that were not listed in the corresponding TA files. 

#Design
Coming soon
