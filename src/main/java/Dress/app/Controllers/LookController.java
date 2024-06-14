package Dress.app.Controllers;

import Dress.app.Mappers.infoFromItems;
import Dress.app.Mappers.infoFromLook;
import Dress.app.Models.Look;
import Dress.app.Requests.LookParametersRequest;
import Dress.app.services.LookService;
import Dress.app.services.SeasonConverter;
import Dress.app.services.StyleConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/looks")
public class LookController {
    private final LookService lookService;
    private final StyleConverter styleConverter;
    private final SeasonConverter seasonConverter;

    @Autowired
    public LookController(LookService lookService, SeasonConverter seasonConverter, StyleConverter styleConverter) {
        this.lookService = lookService;
        this.seasonConverter = seasonConverter;
        this.styleConverter = styleConverter;
    }

    @GetMapping() //получаем все луки юзера
    public ResponseEntity<List<infoFromLook>> getAllLooks(@RequestParam UUID userId) {
        List<Look> looks = lookService.getUsersLooks(userId);
        return ResponseEntity.ok(infoFromLook.createInfo(looks));
    }

    @GetMapping("/new") // используем когда обязательных нет
    public ResponseEntity<List<infoFromItems>> getNewLookNoParameters(@RequestParam UUID userId) {
        Look look = lookService.createLookNoParameters(userId);
        return ResponseEntity.ok(infoFromItems.createInfo(look.getParts()));
    }

    @PostMapping("/parameters")  // крч на это маппинг отправляем, только если есть обязательные сезон, стиль или вещи
    //т.к. если тело пустое - ругается, с пустым телом используем маппинг /new
    public ResponseEntity<List<infoFromItems>> getNewLookWithParameters(
            @RequestParam UUID userId, @RequestBody LookParametersRequest data) {
        Look look = lookService.createLookWithParameters(userId,
                seasonConverter.makeSeasons(data.seasonsNames),
                styleConverter.makeStyles(data.stylesNames),
                data.itemId);
        return ResponseEntity.ok(infoFromItems.createInfo(look.getParts()));
    }
}



