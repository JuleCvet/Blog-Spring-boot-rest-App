var gulp = require('gulp');
var less = require('gulp-less');
var path = require('path');

gulp.task('default', ['less']);

gulp.task('less', function () {
    return gulp.src('./static/less/**/*.less')
        .pipe(less())
        .pipe(gulp.dest('./static/css/'));
});