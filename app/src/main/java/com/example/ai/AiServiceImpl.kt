package com.example.ai

import com.example.domain.model.AfricanCountries
import com.example.domain.model.Platform
import kotlinx.coroutines.delay

class AiServiceImpl : AiService {

    override val isDemoMode: Boolean = true

    override suspend fun generateIdeas(
        topic: String,
        platform: Platform,
        theme: String,
        duration: String,
        style: String,
        country: String,
        city: String,
        language: String
    ): List<String> {
        delay(900) // Realistic creation latency
        val cleanTopic = topic.ifBlank { "l'entrepreneuriat des jeunes" }
        val countryObj = AfricanCountries.findByName(country)
        val currency = countryObj.currency
        val localCity = if (city.isNotBlank()) city else countryObj.defaultCity

        return listOf(
            "1. 🚀 « Comment démarrer un projet avec seulement 25.000 $currency à $localCity » - Étapes concrètes sans fioritures pour $platform.",
            "2. 💡 « L'erreur fatale que 90% des débutants commettent sur $cleanTopic » - Format $duration avec un ton $style axé sur le vécu.",
            "3. 📊 « Étude de cas réelle : Comment cette petite entreprise de $country a triplé son chiffre grâce à $platform ».",
            "4. 🔍 « Coulisses exclusives : Ce que personne ne te montre dans le business de $cleanTopic à $localCity ».",
            "5. ⏱️ « 3 astuces rapides en 45 secondes pour maîtriser $cleanTopic même si tu débutes aujourd'hui »."
        )
    }

    override suspend fun generateHooks(
        topic: String,
        style: String,
        platform: Platform,
        country: String,
        language: String
    ): List<String> {
        delay(800)
        val cleanTopic = topic.ifBlank { "gagner sa vie en ligne" }
        val countryObj = AfricanCountries.findByName(country)

        return listOf(
            "🛑 Arrête de scroller ! Ce qu'on ne t'a jamais dit sur $cleanTopic...",
            "🤫 Si tu habites en ${countryObj.name} et que tu veux réussir, écoute bien ça...",
            "⚠️ 99% des gens échouent avec $cleanTopic parce qu'ils oublient cette seule règle.",
            "💥 En 2026, si tu ne fais pas ça pour ton business, tu perds de l'argent chaque jour.",
            "🎯 J'ai testé pendant 30 jours cette méthode pour $cleanTopic, voici le résultat sans filtre :",
            "👀 Regarde bien jusqu'au bout, la 3e astuce va changer ta façon de voir les choses."
        )
    }

    override suspend fun generateScript(
        topic: String,
        duration: String,
        platform: Platform,
        tone: String,
        country: String,
        language: String
    ): ScriptResult {
        delay(1100)
        val cleanTopic = topic.ifBlank { "démarrer avec un petit budget" }
        val countryObj = AfricanCountries.findByName(country)
        val currency = countryObj.currency

        val intro = "« Salut la famille ! Si toi aussi tu veux te lancer dans $cleanTopic sans te ruiner, prends 60 secondes pour regarder cette vidéo jusqu'au bout. »"

        val body = """
            « Premièrement, arrête d'attendre d'avoir 1 million de $currency pour commencer. Ce qui compte, c'est de valider ton idée sur le terrain avec les moyens du bord.
            
            Deuxièmement, utilise la puissance de $platform : filme ton processus au quotidien, sois transparent et crée une relation de confiance avec ton audience.
            
            Troisièmement, réinvestis tes premiers bénéfices dans la qualité de ton service plutôt que dans le paraître. »
        """.trimIndent()

        val conclusion = "« La réussite n'est pas une question de chance, mais de constance et de discipline quotidienne. »"

        val cta = "« Abonne-toi pour ne pas rater la partie 2 et dis-moi en commentaire depuis quelle ville tu regardes cette vidéo ! »"

        val fullText = """
            🎬 SCRIPT VIDÉO ($platform - $duration - Ton $tone)
            Sujet : $cleanTopic
            Contexte : ${countryObj.name}
            
            ⏱️ [0:00 - 0:05] INTRODUCTION (HOOK)
            $intro
            
            ⚡ [0:05 - 0:45] DÉVELOPPEMENT (VALEUR PRINCIPALE)
            $body
            
            ✨ [0:45 - 0:55] CONCLUSION
            $conclusion
            
            📢 [0:55 - 1:00] CALL TO ACTION
            $cta
        """.trimIndent()

        return ScriptResult(
            title = "Script : $cleanTopic",
            intro = intro,
            body = body,
            conclusion = conclusion,
            callToAction = cta,
            fullFormattedText = fullText
        )
    }

    override suspend fun generateVideoPrompt(
        character: String,
        appearance: String,
        environment: String,
        action: String,
        camera: String,
        lighting: String,
        movement: String,
        style: String
    ): VideoPromptResult {
        delay(950)
        val charDesc = character.ifBlank { "Un jeune créateur de contenu africain charismatique de 24 ans" }
        val appDesc = appearance.ifBlank { "portant une chemise moderne en lin terracotta brodé, coiffure soignée, regard déterminé" }
        val envDesc = environment.ifBlank { "dans un studio moderne et chaleureux à Abidjan avec des plantes tropicales et un néon doré en arrière-plan" }
        val actDesc = action.ifBlank { "présente avec passion son nouveau projet face caméra en souriant et montrant un smartphone récent" }
        val camDesc = camera.ifBlank { "85mm f/1.8 lens, cinematic shallow depth of field, sharp foreground" }
        val lightDesc = lighting.ifBlank { "golden hour warm sunlight filtering through blinds combined with soft rim light" }
        val motionDesc = movement.ifBlank { "slow smooth push-in camera tracking movement, 24fps cinema cadence" }
        val styleDesc = style.ifBlank { "photorealistic, hyper-detailed 8K, cinematic commercial look" }

        val fullPrompt = "Cinematic video shot in 9:16 vertical aspect ratio: $charDesc, $appDesc, situated $envDesc. Action: $actDesc. Camera setup: $camDesc, $lightDesc, $motionDesc. Visual aesthetics: $styleDesc, highly realistic textures, ultra-detailed, vibrant warm African color grade."

        return VideoPromptResult(
            title = "Prompt Vidéo Génératif (9:16)",
            fullPrompt = fullPrompt,
            character = "$charDesc ($appDesc)",
            environment = envDesc,
            camera = camDesc,
            lighting = lightDesc,
            motion = motionDesc
        )
    }

    override suspend fun generateImagePrompt(
        subject: String,
        character: String,
        scenery: String,
        clothing: String,
        pose: String,
        expression: String,
        lighting: String,
        composition: String,
        cameraLens: String,
        style: String,
        format: String
    ): ImagePromptResult {
        delay(900)
        val sub = subject.ifBlank { "Portrait professionnel d'un entrepreneur africain innovant" }
        val char = character.ifBlank { "Homme africain d'une trentaine d'années, visage élégant" }
        val cloth = clothing.ifBlank { "veste blazer contemporaine texturée aux motifs géométriques subtils" }
        val scen = scenery.ifBlank { "bureau lumineux en hauteur surplombant une métropole africaine moderne et animée" }
        val pos = pose.ifBlank { "posture confiante, bras croisés ou tenant une tablette tactile" }
        val exp = expression.ifBlank { "sourire serein et inspirant, regard direct vers l'objectif" }
        val light = lighting.ifBlank { "lumière naturelle douce du matin, reflets dorés chauds" }
        val comp = composition.ifBlank { "règle des tiers, cadrage en plan moyen, bokeh élégant" }
        val lens = cameraLens.ifBlank { "Sony Alpha A7R V, 85mm G-Master f/1.4" }
        val sty = style.ifBlank { "Photographie éditoriale haute définition, magazine Forbes Afrique style" }
        val fmt = format.ifBlank { "--ar 9:16 --v 6.0 --style raw" }

        val fullPrompt = "$sub, featuring $char, wearing $cloth, in $scen. Pose: $pos, facial expression: $exp. Lighting: $light, composition: $comp, shot on $lens. Visual style: $sty $fmt"

        return ImagePromptResult(
            title = "Prompt Image HD : $sub",
            fullPrompt = fullPrompt,
            style = sty,
            composition = comp,
            lighting = light,
            negativePrompt = "blurry, low quality, oversaturated, deformed hands, cartoon, artificial plastic skin",
            parameters = fmt
        )
    }

    override suspend fun generateAdvertisement(
        productName: String,
        price: String,
        description: String,
        targetAudience: String,
        contact: String,
        location: String
    ): AdResult {
        delay(1000)
        val prod = productName.ifBlank { "Gamme de soins capillaires naturels au karité bio" }
        val prc = price.ifBlank { "7.500 FCFA" }
        val desc = description.ifBlank { "Formule artisanale sans produits chimiques qui fortifie et fait briller les cheveux crépus et bouclés" }
        val aud = targetAudience.ifBlank { "Femmes et hommes soucieux de la santé de leurs cheveux naturels" }
        val cont = contact.ifBlank { "+225 07 00 00 00 / WhatsApp" }
        val loc = location.ifBlank { "Abidjan & livraison sous-région" }

        val hook = "🔥 « Tes cheveux méritent le meilleur de la nature africaine ! Fini la casse et les démangeaisons. »"

        val script = """
            « Si tu cherches une solution saine et efficace pour tes cheveux, découvre $prod.
            $desc.
            Testé et approuvé par des centaines de clients satisfaits à $loc.
            Profite de notre offre spéciale à seulement $prc ! »
        """.trimIndent()

        val onScreen = listOf(
            "🌿 100% Ingrédients Naturels Certifiés",
            "✨ Résultats visibles dès 14 jours",
            "💰 Seulement $prc l'unité",
            "🚀 Livraison rapide à $loc",
            "📲 Commande : $cont"
        )

        val cta = "« Clique sur le lien WhatsApp ci-dessous ou écris-nous au $cont pour commander ton pack avant la rupture ! »"

        val postDesc = """
            ✨ Offre spéciale pour $aud !
            
            Découvrez $prod : $desc.
            
            ✅ Prix exclusif : $prc
            📍 Disponible à : $loc
            🚚 Livraison sécurisée à domicile
            
            📲 Commandez vite au $cont
            
            #CommerceAfricain #MadeInAfrica #Promo #${prod.take(15).replace(" ", "")}
        """.trimIndent()

        val fullFormatted = """
            📢 PACK PUBLICITAIRE CLÉ EN MAIN
            Produit : $prod ($prc)
            
            🎯 HOOK D'ACCROCHE :
            $hook
            
            📝 SCRIPT VIDÉO PROMOTIONNELLE :
            $script
            
            📱 TEXTES À INCRUSTER SUR LA VIDÉO :
            ${onScreen.joinToString("\n") { "• $it" }}
            
            ⚡ APPEL À L'ACTION :
            $cta
            
            📄 DESCRIPTION PRÊTE À PUBLIER :
            $postDesc
        """.trimIndent()

        return AdResult(
            productName = prod,
            hook = hook,
            script = script,
            onScreenText = onScreen,
            callToAction = cta,
            postDescription = postDesc,
            fullFormattedText = fullFormatted
        )
    }

    override suspend fun generateStory(
        idea: String,
        country: String
    ): StoryResult {
        delay(1200)
        val cleanIdea = idea.ifBlank { "Un jeune diplômé qui crée un service de livraison écologique" }
        val countryObj = AfricanCountries.findByName(country)
        val currency = countryObj.currency
        val city = countryObj.defaultCity

        val title = "Le Pari de l'Avenir à $city"
        val chars = "Amadou (22 ans, inventif et déterminé), Papa Ousmane (commerçant respecté au marché)"
        val ctx = "Dans les rues vibrantes de $city (${countryObj.name}), où les embouteillages ralentissent l'économie locale."
        val beginning = "Fraîchement diplômé mais sans piston, Amadou observe chaque jour les mères de famille et restaurateurs perdre des heures pour se ravitailler. Avec sa vieille bicyclette retapée et une glacière isotherme achetée avec ses 15.000 $currency d'économies, il propose ses premières livraisons."
        val dev = "Pendant un mois de canicule et de pluies battantes, il ne renonce jamais. Il met en place un numéro de commande simple et garantit une livraison ponctuelle. Très vite, trois restaurants réputés de $city lui confient toutes leurs livraisons."
        val climax = "Lors de la fête nationale, une commande colossale de 200 repas doit être acheminée en 40 minutes alors que la ville entière est bloquée. Amadou mobilise 5 amis cyclistes du quartier et coordonne le convoi à travers les ruelles. La commande arrive à la minute près, chaude et intacte."
        val ending = "Le client prestigieux n'était autre qu'un grand entrepreneur de la capitale, qui décide de financer sa flotte de vélos-cargos électriques. Aujourd'hui, Amadou gère une équipe de 18 jeunes."
        val moral = "La valeur d'un entrepreneur ne se mesure pas à ses diplômes ou à ses relations, mais à sa capacité à résoudre un vrai problème dans son environnement avec courage."

        val fullText = """
            📖 HISTOIRE COMPLÈTE : $title
            
            👥 Personnages : $chars
            🌍 Contexte : $ctx
            
            🌱 1. LE DÉBUT
            $beginning
            
            ⚡ 2. LE DÉVELOPPEMENT
            $dev
            
            🔥 3. LE CLIMAX (MOMENT CRUCIAL)
            $climax
            
            🏆 4. LE DÉNOUEMENT
            $ending
            
            💡 5. LA MORALE
            $moral
        """.trimIndent()

        return StoryResult(
            title = title,
            characters = chars,
            context = ctx,
            beginning = beginning,
            development = dev,
            climax = climax,
            ending = ending,
            moral = moral,
            fullFormattedText = fullText
        )
    }

    override suspend fun generateHashtags(
        topic: String,
        platform: Platform,
        country: String
    ): List<String> {
        delay(600)
        val cleanTopic = topic.ifBlank { "business" }.replace(" ", "").lowercase()
        val countryObj = AfricanCountries.findByName(country)
        val cName = countryObj.name.replace(" ", "").replace("'", "").lowercase()

        return listOf(
            "#AfricaCreator",
            "#CréateursAfricains",
            "#Afrique2026",
            "#Team$cName",
            "#BusinessAfrique",
            "#$cleanTopic",
            "#Viral$cleanTopic",
            "#PourToiAfrique",
            "#FYPAfrica",
            "#InspirationAfrique",
            "#MadeInAfrica",
            "#ReussiteAfricaine"
        )
    }

    override suspend fun generateQuickPack(
        idea: String,
        country: String,
        platform: Platform
    ): QuickPackResult {
        delay(1300)
        val cleanIdea = idea.ifBlank { "Un jeune créateur africain qui lance sa marque avec son smartphone" }
        val countryObj = AfricanCountries.findByName(country)
        val city = countryObj.defaultCity
        val currency = countryObj.currency

        val hooks = listOf(
            "🛑 Arrête de chercher des excuses : voici comment $cleanIdea à $city !",
            "🤫 La vérité que personne n'ose dire sur $cleanIdea...",
            "⚡ En 3 étapes simples, tu peux lancer ça avec moins de 20.000 $currency !"
        )

        val script = ScriptResult(
            title = "Vidéo : $cleanIdea",
            intro = "« Tu penses qu'il faut des millions pour réussir ? Laisse-moi te prouver le contraire en 45 secondes. »",
            body = "« Voici les 3 secrets clés : 1. Commence petit mais vise grand. 2. Utilise $platform pour raconter ton histoire authentique sans filtre. 3. Sois régulier chaque semaine sans abandonner. »",
            conclusion = "« Le meilleur moment pour commencer, c'était hier. Le deuxième meilleur moment, c'est aujourd'hui. »",
            callToAction = "« Écris 'GO' en commentaire si tu es prêt à relever le défi et abonne-toi ! »",
            fullFormattedText = "Vidéo complète préparée pour $platform sur : $cleanIdea"
        )

        val videoPrompt = "Cinematic 9:16 vertical video of an energetic young African creator in $city holding a smartphone with ring light reflection, vibrant warm lighting, 4K photorealistic, shot on 35mm lens, golden amber aesthetic."
        val imagePrompt = "A stylized modern graphic illustration of creative African digital creators collaborate with smartphones and mics, warm sunset colors, minimalist flat vector art --ar 9:16"

        val scenes = listOf(
            "Scène 1 (0-3s) : Plan serré face caméra, expression intense, hook percutant avec sous-titres animés.",
            "Scène 2 (4-15s) : B-roll montrant le travail concret, l'écran du smartphone et les préparatifs du projet.",
            "Scène 3 (16-30s) : Plan moyen avec gestuelle explicative, infographies simples à l'écran.",
            "Scène 4 (31-45s) : Sourire complice, geste vers le bouton s'abonner avec audio viral en fond sonore."
        )

        val hashtags = listOf(
            "#AfricaCreator",
            "#Afrique",
            "#${countryObj.name.replace(" ", "")}",
            "#$platform",
            "#Entrepreneuriat",
            "#Creativite"
        )

        val desc = """
            🚀 $cleanIdea
            
            Prêt à passer à l'action ? Tous les détails sont dans la vidéo.
            Dis-moi en commentaire ce que tu en penses 👇
            
            ${hashtags.joinToString(" ")}
        """.trimIndent()

        return QuickPackResult(
            originalIdea = cleanIdea,
            hooks = hooks,
            script = script,
            videoPrompt = videoPrompt,
            imagePrompt = imagePrompt,
            scenes = scenes,
            hashtags = hashtags,
            description = desc
        )
    }
}
