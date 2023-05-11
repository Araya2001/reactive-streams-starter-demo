package aaj;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
  public static void main(String[] args) {
    Mono<String> stringMono; // UN SOLO OBJETO, UNA SOLA SUSCRIPCIÓN
    Flux<String> stringFlux; // MÚLTIPLES OBJETOS, MÚLTIPLES SUSCRIPCIONES
    List<String> list = new ArrayList<>();
    System.out.println("\nEJEMPLO 1:\n");
    // -------------------------------------------------------------
    // EJEMPLO 1, CONSUMIR STREAMS MONO<T>
    // INICIAR STREAM DE UN SOLO OBJETO
    stringMono = Mono.just("Alejandro");
    // CONSUMIR STREAM (REFERENCIA DEL MÉTODO)
    stringMono.subscribe(System.out::println);
    // RECREAR STREAM DE UN SOLO OBJETO
    stringMono = Mono.just("stream");
    // CONSUMIR STREAM (LAMBDA)
    stringMono.subscribe(s -> System.out.println(s.toUpperCase()));
    // -------------------------------------------------------------
    System.out.println("\nEJEMPLO 2:\n");
    // EJEMPLO 2, MUTAR STREAM Y CONSUMIR STREAM
    stringMono = Mono.just("STRING MONO");
    stringMono.map(s -> s.toLowerCase().replace(" ", "-") + " MUTATION")
        .subscribe(System.out::println);
    // -------------------------------------------------------------
    System.out.println("\nEJEMPLO 3:\n");
    // EJEMPLO 3, CALLBACK DO ON NEXT
    stringMono = Mono.just("NEXT STRING");
    stringMono.doOnNext(s -> System.out.println(s + " - DO ON NEXT CALLBACK"))
        .map(s -> s + " ON MAP")
        .subscribe(System.out::println);
    // -------------------------------------------------------------
    System.out.println("\nEJEMPLO 4:\n");
    // EJEMPLO 4, MUTAR STREAM CON FLATMAP
    stringMono = Mono.just("STRING WITH FLATMAP");
    stringMono.flatMap(s -> Mono.just(s + " MUTATION"))
        .subscribe(System.out::println);
    // -------------------------------------------------------------
    // EJEMPLO 5, MULTIPLES OBJETOS Y MUTACIÓN
    System.out.println("\nEJEMPLO 5:\n");
    stringFlux = Flux.just("STRING-1", "STRING-2", "STRING-3");
    stringFlux
        .doOnNext(System.out::println)
        .map(s -> s.replace("-", "---"))
        .doOnNext(System.out::println)
        .subscribe(list::add);
    // -------------------------------------------------------------
    // EJEMPLO 6, MULTIPLES OBJETOS DESDE ITERABLES
    System.out.println("\nEJEMPLO 6:\n");
    list.addAll(Arrays.asList("AS LIST - STRING-1", "AS LIST - STRING-2", "AS LIST - STRING-3", "AS LIST - STRING-4", "AS LIST - STRING-5"));
    Flux.fromIterable(list)
        .map(String::toLowerCase)
        .window(5)
        .delayElements(Duration.ofMillis(3000))
        .flatMap(stringFlux1 ->
            stringFlux1
                .parallel()
                .map(s -> s + " : map PARALLEL DELAYED WINDOWED FLUX")
                .sequential()
        ).subscribe(System.out::println);
    try {
      Thread.sleep(10000);
    } catch (Exception e) {
      System.err.println("Caught Exception: " + e);
    }
  }
}
