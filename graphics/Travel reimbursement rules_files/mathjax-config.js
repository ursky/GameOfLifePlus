/*
* MathJax configuration
* http://docs.mathjax.org/en/latest/options/index.html
* Currently only support Tex/LaTex
* tex2jax
*  - inlineMath - Array of pairs of strings that are to be used as in-line math delimiters
*  - displayMath - Array of pairs of strings that are to be used as delimiters for displayed equations
*  - processEscapes - When set to true, you may use \$ to represent a literal dollar sign, rather than using it as a math delimiter.
*  - ignoreClass - element whose content should not be processed by tex2jax, default to xwiki main content area
*  - processClass - elements whose contents should be processed by tex2jax, we will inject the class in {{formula}}
*/

MathJax.Hub.Config({
  extensions: ["tex2jax.js"],
  jax: ["input/TeX", "output/CommonHTML"],
  messageStyle: "none",
  tex2jax: {
    inlineMath: [ ['$','$'] ],
    displayMath: [ ['$$','$$'] ],
    processEscapes: true,
    ignoreClass: "main",
    processClass: "mathjax-content-latex"
  }
});
