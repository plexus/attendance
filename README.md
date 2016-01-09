# attendance

## Environment

You need a "google service account", see links below. When you create the
account you get a "client ID" which looks like an email address. You can share
the spreadsheets you want the app to access with this email, just as if you
would share access with another person.

* ATTENDANCE_CLIENT_ID: email address of the google service account
* ATTENDANCE_P12_KEY: base64 encoded P12 key file, no wrapping

e.g.

```
export ATTENDANCE_P12_KEY=$(cat ~/my-app.p12 | base64 -w 0)
```

## Development

Open a terminal and type `lein repl` to start a Clojure REPL
(interactive prompt).

In the REPL, type

```clojure
(run)
(browser-repl)
```

The call to `(run)` does two things, it starts the webserver at port
10555, and also the Figwheel server which takes care of live reloading
ClojureScript code and CSS. Give them some time to start.

Running `(browser-repl)` starts the Weasel REPL server, and drops you
into a ClojureScript REPL. Evaluating expressions here will only work
once you've loaded the page, so the browser can connect to Weasel.

When you see the line `Successfully compiled "resources/public/app.js"
in 21.36 seconds.`, you're ready to go. Browse to
`http://localhost:10555` and enjoy.

**Attention: It is not longer needed to run `lein figwheel`
  separately. This is now taken care of behind the scenes**

## Trying it out

If all is well you now have a browser window saying 'Hello Chestnut',
and a REPL prompt that looks like `cljs.user=>`.

Open `resources/public/css/style.css` and change some styling of the
H1 element. Notice how it's updated instantly in the browser.

Open `src/cljs/attendance/core.cljs`, and change `dom/h1` to
`dom/h2`. As soon as you save the file, your browser is updated.

In the REPL, type

```
(ns attendance.core)
(swap! app-state assoc :text "Interactivity FTW")
```

Notice again how the browser updates.

## Deploying to Heroku

This assumes you have a
[Heroku account](https://signup.heroku.com/dc), have installed the
[Heroku toolbelt](https://toolbelt.heroku.com/), and have done a
`heroku login` before.

``` sh
git init
git add -A
git commit
heroku create
git push heroku master:master
heroku open
```

## Running with Foreman

Heroku uses [Foreman](http://ddollar.github.io/foreman/) to run your
app, which uses the `Procfile` in your repository to figure out which
server command to run. Heroku also compiles and runs your code with a
Leiningen "production" profile, instead of "dev". To locally simulate
what Heroku does you can do:

``` sh
lein with-profile -dev,+production uberjar && foreman start
```

Now your app is running at
[http://localhost:5000](http://localhost:5000) in production mode.

## Links

* [Google sheets API docs](https://developers.google.com/google-apps/spreadsheets/)

Regarding Google Service account

* [StackOverflow outlining the process](http://stackoverflow.com/questions/31507989/java-to-google-spreadsheet/31527352#31527352)
* [Official google instructions for creating an account](https://developers.google.com/identity/protocols/OAuth2ServiceAccount)


## License

Copyright © 2016 Arne Brasseur

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.

## Chestnut

Created with [Chestnut](http://plexus.github.io/chestnut/) 0.9.0-SNAPSHOT (c91ddacf).
