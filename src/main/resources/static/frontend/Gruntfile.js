// Generated on 2015-08-11 using generator-angular 0.11.1
'use strict';

// # Globbing
// for performance reasons we're only matching one level down:
// 'test/spec/{,*/}*.js'
// use this if you want to recursively match all subfolders:
// 'test/spec/**/*.js'

module.exports = function (grunt) {

  // Load grunt tasks automatically
  require('load-grunt-tasks')(grunt);

  // Time how long tasks take. Can help when optimizing build times
  require('time-grunt')(grunt);

  // Configurable paths for the application
  var appConfig = {
    app: require('./bower.json').appPath || 'app',
    dist: 'dist'
  };

  // Define the configuration for all the tasks
  grunt.initConfig({

    // Project settings
    yeoman: appConfig,

    protractor_webdriver: {
        protractor: {
            options: {
                path: './node_modules/grunt-protractor-runner/node_modules/protractor/bin/',
                command: 'webdriver-manager start --standalone'
            }
        }
     },

      protractor: {
          options: {
              configFile: "app/tests/e2e/e2e-conf.js",
              noColor: false
          },
          all: {
          }
      },

    // Watches files for changes and runs tasks based on the changed files
    watch: {
      bower: {
        files: ['bower.json']
        //tasks: ['wiredep']
      },
      ngconstant: {
        files: ['Gruntfile.js'],
        tasks: ['ngconstant:dev']
      },
      js: {
        files: ['<%= yeoman.app %>/scripts/{,*/}*.js'],
        tasks: ['newer:jshint:all'],
        options: {
          livereload: '<%= connect.options.livereload %>'
        }
      },
      jsTest: {
        files: ['test/{,*/}*.js'],
        tasks: ['newer:jshint:test', 'karma']
      },
      styles: {
        files: ['<%= yeoman.app %>/styles/{,*/}*.css'],
        tasks: ['newer:copy:styles', 'autoprefixer']
      },
      gruntfile: {
        files: ['Gruntfile.js']
      },
      livereload: {
        options: {
          livereload: '<%= connect.options.livereload %>'
        },
        files: [
          '<%= yeoman.app %>/{,*/}*.html',
          '.tmp/styles/{,*/}*.css',
          '<%= yeoman.app %>/images/{,*/}*.{png,jpg,jpeg,gif,webp,svg}'
        ]
      }
    },

    // The actual grunt server settings
    connect: {
      options: {
        port: 9002,
        // Change this to '0.0.0.0' to access the server from outside.
        hostname: '155.105.202.58',
        livereload: 35730
      },
      livereload: {
        options: {
          open: true,
          middleware: function (connect) {
            return [
              connect.static('.tmp'),
              connect().use(
                '/app/bower_components',
                connect.static('./app/bower_components')
              ),
              connect().use(
                '/app/styles',
                connect.static('./app/styles')
              ),
              connect.static(appConfig.app)
            ];
          }
        }
      },
      test: {
        options: {
          port: 9011,
          middleware: function (connect) {
            return [
              connect.static('.tmp'),
              connect.static('test'),
              connect().use(
                '/app/bower_components',
                connect.static('./app/bower_components')
              ),
              connect.static(appConfig.app)
            ];
          }
        }
      },
      dist: {
        options: {
          open: true,
          base: '<%= yeoman.dist %>'
        }
      }
    },

    // Make sure code styles are up to par and there are no obvious mistakes
    jshint: {
      options: {
        jshintrc: '.jshintrc',
        reporter: require('jshint-stylish')
      },
      all: {
        src: [
          'Gruntfile.js',
          '<%= yeoman.app %>/scripts/{,*/}*.js'
        ]
      },
      test: {
        options: {
          jshintrc: 'test/.jshintrc'
        },
        src: ['test/spec/{,*/}*.js']
      }
    },

    // Empties folders to start fresh
    clean: {
      dist: {
        files: [{
          dot: true,
          src: [
            '.tmp',
            '<%= yeoman.dist %>/{,*/}*',
            '!<%= yeoman.dist %>/.git{,*/}*'
          ]
        }]
      },
      server: '.tmp',
      screenshots:'screenshots'
    },

    // Add vendor prefixed styles
    autoprefixer: {
      options: {
        browsers: ['last 1 version']
      },
      server: {
        options: {
          map: true
        },
        files: [{
          expand: true,
          cwd: '.tmp/styles/',
          src: '{,*/}*.css',
          dest: '.tmp/styles/'
        }]
      },
      dist: {
        files: [{
          expand: true,
          cwd: '.tmp/styles/',
          src: '{,*/}*.css',
          dest: '.tmp/styles/'
        }]
      }
    },

    // Automatically inject Bower components into the app
    wiredep: {
      app: {
        src: ['<%= yeoman.app %>/index.html'],
        ignorePath:  /\.\.\//
      },
      test: {
        devDependencies: true,
        src: '<%= karma.unit.configFile %>',
        ignorePath:  /\.\.\//,
        fileTypes:{
          js: {
            block: /(([\s\t]*)\/{2}\s*?bower:\s*?(\S*))(\n|\r|.)*?(\/{2}\s*endbower)/gi,
              detect: {
                js: /'(.*\.js)'/gi
              },
              replace: {
                js: '\'{{filePath}}\','
              }
            }
          }
      }
    },

    // Renames files for browser caching purposes
    filerev: {
      dist: {
        src: [
          '<%= yeoman.dist %>/scripts/{,*/}*.js',
          '<%= yeoman.dist %>/styles/{,*/}*.css',
          //'<%= yeoman.dist %>/images/{,*/}*.{png,jpg,jpeg,gif,webp,svg}',
          '<%= yeoman.dist %>/styles/fonts/*'
        ]
      }
    },

    // Reads HTML for usemin blocks to enable smart builds that automatically
    // concat, minify and revision files. Creates configurations in memory so
    // additional tasks can operate on them
    useminPrepare: {
      html: '<%= yeoman.app %>/index.html',
      options: {
        dest: '<%= yeoman.dist %>',
        flow: {
          html: {
            steps: {
              js: ['concat', 'uglifyjs'],
              css: ['cssmin']
            },
            post: {}
          }
        }
      }
    },

    // Performs rewrites based on filerev and the useminPrepare configuration
    usemin: {
      html: ['<%= yeoman.dist %>/{,*/}*.html'],
      css: ['<%= yeoman.dist %>/styles/{,*/}*.css'],
      options: {
        assetsDirs: [
          '<%= yeoman.dist %>',
          '<%= yeoman.dist %>/images',
          '<%= yeoman.dist %>/styles'
        ]
      }
    },

    imagemin: {
      dist: {
        files: [{
          expand: true,
          cwd: '<%= yeoman.app %>/images',
          src: '{,*/}*.{png,jpg,jpeg,gif}',
          dest: '<%= yeoman.dist %>/images'
        }]
      }
    },

    svgmin: {
      dist: {
        files: [{
          expand: true,
          cwd: '<%= yeoman.app %>/images',
          src: '{,*/}*.svg',
          dest: '<%= yeoman.dist %>/images'
        }]
      }
    },

    htmlmin: {
      dist: {
        options: {
          collapseWhitespace: true,
          conservativeCollapse: true,
          collapseBooleanAttributes: true,
          removeCommentsFromCDATA: true,
          removeOptionalTags: true
        },
        files: [{
          expand: true,
          cwd: '<%= yeoman.dist %>',
          src: ['*.html', 'views/{,*/}*.html'],
          dest: '<%= yeoman.dist %>'
        }]
      }
    },

    // ng-annotate tries to make the code safe for minification automatically
    // by using the Angular long form for dependency injection.
    ngAnnotate: {
      dist: {
        files: [{
          expand: true,
          cwd: '.tmp/concat/scripts',
          src: '*.js',
          dest: '.tmp/concat/scripts'
        }]
      }
    },
    //create angular configuration file
      ngconstant: {
          options: {
              name: 'app.config',
              dest: 'app/scripts/app/app.config.js',
              constants: {
                  backendUrl: 'BACKEND-URL',
                  backendExportChartUrl: '<%= ngconstant.options.constants.backendUrl %>/exportingChart.php',
                  dropboxAppkey: 'OPT DROPBOX!!!!'
              }
          },
          test: {
          },
          dev: {
              constants: {
                  backendUrl: 'http://155.105.202.58:8080',
                  backendExportChartUrl: '<%= ngconstant.dev.constants.backendUrl %>/exportingChart.php',
                  dropboxAppkey: '7wew0rj0gh2qcik'
              }
          },
          int: {
              constants: {
                  backendUrl: 'http://hbps1.chuv.ch/mip/services',
                  backendExportChartUrl: '<%= ngconstant.int.constants.backendUrl %>/exportingChart.php',
                  dropboxAppkey: '7wew0rj0gh2qcik'
              }
          },
          demo: {
              constants: {
                backendUrl: 'http://chuv-backend.redfroggy.fr',
                backendExportChartUrl: '<%= ngconstant.demo.constants.backendUrl %>/exportingChart.php',
                dropboxAppkey: '7wew0rj0gh2qcik'
              }
          },
          prod: {
              constants: {
                backendUrl: 'http://hbp-mip.chuv.ch/services/backend',
                backendExportChartUrl: '<%= ngconstant.prod.constants.backendUrl %>/exportingChart.php',
                dropboxAppkey: 'PROD_DROPBOX_APPKEY'
              }
          }
      },

    // Replace Google CDN references
    cdnify: {
      dist: {
        html: ['<%= yeoman.dist %>/*.html']
      }
    },

    'string-replace': {
      inline: {
        files: {
          '<%= yeoman.dist %>/': '<%= yeoman.dist %>/index.html'
        },
        options: {
          replacements: [
            {
              pattern: "%DropBoxAppKey%",
              replacement: "<%= ngconstant.options.constants.dropboxAppkey %>"
            }
          ]
        }
      },
      dev: {
        files: {
          '<%= yeoman.app %>/': '<%= yeoman.app %>/index.html',
          '<%= yeoman.dist %>/': '<%= yeoman.dist %>/index.html'
        },
        options: {
          replacements: [
            {
              pattern: "%DropBoxAppKey%",
              replacement: "<%= ngconstant.dev.constants.dropboxAppkey %>"
            }
          ]
        }
      },
      int: {
        files: {
          '<%= yeoman.dist %>/': '<%= yeoman.dist %>/index.html'
        },
        options: {
          replacements: [
            {
              pattern: "%DropBoxAppKey%",
              replacement: "<%= ngconstant.int.constants.dropboxAppkey %>"
            }
          ]
        }
      },
      demo: {
        files: {
          '<%= yeoman.dist %>/': '<%= yeoman.dist %>/index.html'
        },
        options: {
          replacements: [
            {
              pattern: "%DropBoxAppKey%",
              replacement: "<%= ngconstant.demo.constants.dropboxAppkey %>"
            }
          ]
        }
      },
      prod: {
        files: {
          '<%= yeoman.dist %>/': '<%= yeoman.dist %>/index.html'
        },
        options: {
          replacements: [
            {
              pattern: "%DropBoxAppKey%",
              replacement: "<%= ngconstant.prod.constants.dropboxAppkey %>"
            }
          ]
        }
      }
    },

    // Copies remaining files to places other tasks can use
    copy: {
      dist: {
        files: [{
          expand: true,
          dot: true,
          cwd: '<%= yeoman.app %>',
          dest: '<%= yeoman.dist %>',
          src: [
            '*.{ico,png,txt}',
            '.htaccess',
            '*.html',
            'scripts/**/{,*/}*.html',
            'images/{,*/}*.{webp}',
            'i18n/{,*/}*.json',
            'i18n/{,*/}*.js',
            'font/**',
            'styles/**/{,*/}*.css',
            'styles/**/{,*/}*.less',
            'styles/**/{,*/}*.js'
          ]
        }, {
          expand: true,
          cwd: '.tmp/images',
          dest: '<%= yeoman.dist %>/images',
          src: ['generated/*']
        }, {
          expand: true,
          cwd: 'app/bower_components/bootstrap/dist',
          src: 'fonts/*',
          dest: '<%= yeoman.dist %>'
        },{
            expand: true,
            cwd: 'app/bower_components/tinymce-dist',
            src: '*/**',
            dest: '<%= yeoman.dist %>/scripts'
        },{
            expand: true,
            cwd: '<%= yeoman.app %>/libs',
            src: '*/**',
            dest: '<%= yeoman.dist %>/libs'
        }, {
          expand: true,
          cwd: 'app/bower_components/themify-icons',
          src: 'fonts/*',
          dest: '<%= yeoman.dist %>/styles'
        }, {
          expand: true,
          cwd: 'app/bower_components/font-awesome',
          src: 'fonts/*',
          dest: '<%= yeoman.dist %>'
        }]
      },
      styles: {
        expand: true,
        cwd: '<%= yeoman.app %>/styles',
        dest: '.tmp/styles/',
        src: '{,*/}*.css'
      }
    },
    processhtml: {
      options: {
          commentMarker: 'prochtml',
          process: true
      },
      dist: {
          files: {
              '<%= yeoman.dist %>/index.html': ['<%= yeoman.dist %>/index.html']
          }
      }
    },
    less: {
      server: {
          options: {
              // strictMath: true,
              dumpLineNumbers: true,
              sourceMap: true,
              sourceMapRootpath: '',
              outputSourceFiles: true
          },
          files: [
              {
                  expand: true,
                  cwd: '<%= yeoman.app %>/styles/less',
                  src: 'styles.less',
                  dest: '.tmp/styles/css',
                  ext: '.css'
              }
          ]
      },
      dist: {
          options: {
              cleancss: true,
              report: 'min'
          },
          files: [
              {
                  expand: true,
                  cwd: '<%= yeoman.app %>/styles/less',
                  src: 'styles.less',
                  dest: '.tmp/styles/css',
                  ext: '.css'
              }
          ]
      }
  },

    // Run some tasks in parallel to speed up the build process
    concurrent: {
      server: [
        'copy:styles'
      ],
      test: [
        'copy:styles'
      ],
      dist: [
        'copy:styles',
        'imagemin',
        'svgmin'
      ]
    },

    // Test settings
    karma: {
      unit: {
        configFile: 'karma.conf.js',
        singleRun: true
      }
    }
  });


  grunt.registerTask('serve', 'Compile then start a connect web server', function (target) {
    if (target === 'dist') {
      return grunt.task.run(['build', 'connect:dist:keepalive']);
    }

    grunt.task.run([
      'clean:server',
      'ngconstant:dev',
      'concurrent:server',
      'autoprefixer:server',
      'connect:livereload',
      'watch'
    ]);
  });

  grunt.registerTask('server', 'DEPRECATED TASK. Use the "serve" task instead', function (target) {
    grunt.log.warn('The `server` task has been deprecated. Use `grunt serve` to start a server.');
    grunt.task.run(['serve:' + target]);
  });

  grunt.registerTask('test', [
    'clean:server',
    'ngconstant:test',
    'concurrent:test',
    'autoprefixer',
    'connect:test',
    'karma'
  ]);

  grunt.registerTask('build', [
    'clean:dist',
    'useminPrepare',
    'concurrent:dist',
    'less:dist',
    'autoprefixer',
    'concat',
    'ngAnnotate',
    'copy:dist',
    'cdnify',
    'cssmin',
    'uglify',
    'filerev',
    'usemin',
    'processhtml:dist',
    'htmlmin'
  ]);

    /**
     * Build by environment (Ex: grunt-build:integration)
     * Generate a config.js file
     */
    grunt.registerTask('build-env',function(env){
        if(env == null){
            env  ="dev";
        }
        grunt.task.run(['ngconstant:'+env+'','build', 'string-replace:'+env+'']);
    });

  grunt.registerTask('default', [
    'newer:jshint',
    'test',
    'build'
  ]);

  grunt.registerTask('funcTests', [
      'clean:screenshots',
      'protractor_webdriver',
      'protractor'
  ]);

  grunt.loadNpmTasks('grunt-string-replace');
};
