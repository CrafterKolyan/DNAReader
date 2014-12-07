#ifndef ELIBSOURCES
#define ELIBSOURCES
#endif

#ifdef ELIBSOURCES
#define pcre_malloc epcre_malloc
#define pcre_free epcre_free
#define pcre_stack_malloc epcre_stack_malloc
#define pcre_stack_free epcre_stack_free
#define pcre_callout epcre_callout
#define pcre_compile epcre_compile
#define pcre_compile2 epcre_compile2
#define pcre_config epcre_config
#define pcre_copy_named_substring epcre_copy_named_substring
#define pcre_copy_substring epcre_copy_substring
#define pcre_dfa_exec epcre_dfa_exec
#define pcre_free_substring epcre_free_substring
#define pcre_free_substring_list epcre_free_substring_list
#define pcre_fullinfo epcre_fullinfo
#define pcre_get_named_substring epcre_get_named_substring
#define pcre_get_stringnumber epcre_get_stringnumber
#define pcre_get_stringtable_entries epcre_get_stringtable_entries
#define pcre_get_substring epcre_get_substring
#define pcre_get_substring_list epcre_get_substring_list
#define pcre_info epcre_info
#define pcre_maketables epcre_maketables
#define pcre_refcount epcre_refcount
#define pcre_study epcre_study
#define pcre_version epcre_version
#endif
