(define-syntax trace-ex
  (syntax-rules ()
    ((_ expr)
     (begin
       (display 'expr)
       (display " => ")
       (let ((result expr))
         (write result)
         (newline)
         result)))))