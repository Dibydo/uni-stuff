(define-syntax my-let
  (syntax-rules ()
    ((_ ((name var) ...) part1 . part2)
     ((lambda (name ...) part1 . part2)
      var ...))))

(define-syntax my-let*
  (syntax-rules ()
    ((_ () part1 . part2)
     (my-let () part1 . part2))
    ((_ ((name1 var1) (name2 var2) ...) part1 . part2)
     (my-let ((name1 var1))
             (my-let* ((name2 var2) ...)
                      part1 . part2)))))